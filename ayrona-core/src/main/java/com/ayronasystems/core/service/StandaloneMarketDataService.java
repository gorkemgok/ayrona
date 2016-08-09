package com.ayronasystems.core.service;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.configuration.ConfKey;
import com.ayronasystems.core.configuration.Configuration;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.MarketDataModel;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.data.MarketDataCache;
import com.ayronasystems.core.data.MarketDataCacheResult;
import com.ayronasystems.core.data.OHLC;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.definition.SymbolPeriod;
import com.ayronasystems.core.definition.Symbols;
import com.ayronasystems.core.exception.CorruptedMarketDataException;
import com.ayronasystems.core.exception.MarketDataConversionException;
import com.ayronasystems.core.util.DateUtils;
import com.ayronasystems.core.util.Interval;
import com.ayronasystems.core.util.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 21/05/16.
 */
public class StandaloneMarketDataService implements MarketDataService {

    private static Configuration conf = Configuration.getInstance ();

    private static Logger log = LoggerFactory.getLogger (StandaloneMarketDataService.class);

    private static volatile MarketDataService marketDataService;

    private List<OHLC> ohlcList = new ArrayList<OHLC> ();

    private static Object lock = new Object ();

    private Dao dao = Singletons.INSTANCE.getDao ();

    private MarketDataCache cache;

    public static MarketDataService getInstance () {
        if (marketDataService == null){
            synchronized (lock){
                if (marketDataService == null) {
                    marketDataService = new StandaloneMarketDataService ();
                }
            }
        }
        return marketDataService;
    }

    private StandaloneMarketDataService () {
        cache = new MarketDataCache();
    }

    public MarketData getOHLC (Symbol symbol, Period period, Date endDate, int count) {
        return fetchOHLC (symbol, period, endDate, count);
    }

    public MarketData getOHLC (Symbol symbol, Period period, Date startDate, Date endDate) {
        MarketData marketData = getOHLCBetweenDates (symbol, period, startDate, endDate);
        List<Interval> absentIntervalList = marketData.getInterval ().extractNot (new Interval (startDate, endDate));
        if (period.getAsMillis () > Period.M1.getAsMillis () ) {
            for ( Interval absentInterval : absentIntervalList ) {
                MarketData m1MarketData = getOHLCBetweenDates (symbol, Period.M1, absentInterval.getBeginningDate (), absentInterval.getEndingDate ());
                try {
                    MarketData convertedMarketData = m1MarketData.convert (period);
                    marketData = marketData.safeMerge (convertedMarketData);
                } catch ( MarketDataConversionException e ) {
                    log.error (e.getMessage (), e);
                }
            }
        }
        return marketData;
    }

    private MarketData getOHLCBetweenDates (Symbol symbol, Period period, Date startDate, Date endDate) {
        long start = System.currentTimeMillis();
        MarketDataCacheResult cacheResult = cache.search(
                new SymbolPeriod(symbol, period),
                new Interval(startDate, endDate)
        );
        long end = System.currentTimeMillis();
        if (cacheResult.hasData()) {
            log.info("Loaded {} market data from cache {}, {} between {}, {} in {} ms",
                    cacheResult.getFoundData ().size (),
                    symbol, period,
                    DateUtils.formatDate(cacheResult.getFoundData().getBeginningDate()),
                    DateUtils.formatDate(cacheResult.getFoundData().getEndingDate()),
                    (end-start));
            if (cacheResult.hasAbsentData()){
                MarketData mainMd = cacheResult.getFoundData();
                for (Interval absentInterval : cacheResult.getAbsentIntervals()){
                    MarketData md = fetchOHLC(
                            symbol, period,
                            absentInterval.getBeginningDate(), absentInterval.getEndingDate()
                    );
                    mainMd = mainMd.merge(md);
                }
                cache.cache(mainMd);
                return mainMd;
            }else{
                return cacheResult.getFoundData();
            }
        } else{
            MarketData marketData = fetchOHLC(symbol, period, startDate, endDate);
            cache.cache(marketData);
            return marketData;
        }
    }

    public MarketData fetchOHLC (Symbol symbol, Period period, Date startDate, Date endDate) {
        long start = System.currentTimeMillis();
        List<MarketDataModel> marketDataModelList = dao.findMarketData (symbol, period, startDate, endDate);
        OHLC ohlc = createOHLC (symbol, period, marketDataModelList);
        long end = System.currentTimeMillis();
        log.info("Fetched market {} data {}, {} between {}, {} in {} ms",
                ohlc.size (),
                symbol, period,
                DateUtils.formatDate(startDate),
                DateUtils.formatDate(endDate),
                (end-start));
        return ohlc;
    }

    public MarketData fetchOHLC (Symbol symbol, Period period, Date endDate, int count) {
        long start = System.currentTimeMillis();
        List<MarketDataModel> marketDataModelList = dao.findMarketData (symbol, period, endDate, count);
        OHLC ohlc = createOHLC (symbol, period, marketDataModelList);
        long end = System.currentTimeMillis();
        log.info("Fetched market {} data {}, ending date: {}, count: {} in {} ms",
                 ohlc.size (),
                 symbol, period,
                 DateUtils.formatDate(endDate),
                 count,
                 (end-start));
        return ohlc;
    }

    private static OHLC createOHLC (Symbol symbol, Period period, List<MarketDataModel> marketDataModelList) {
        int size = marketDataModelList.size ();
        List<Date> dates = new ArrayList<Date> (size);
        double[] open = new double[size];
        double[] high = new double[size];
        double[] low = new double[size];
        double[] close = new double[size];
        int i = 0;
        for (MarketDataModel marketDataModel : marketDataModelList) {
            dates.add (marketDataModel.getPeriodDate ());
            open[i] = marketDataModel.getOpen ();
            high[i] = marketDataModel.getHigh ();
            low[i] = marketDataModel.getLow ();
            close[i] = marketDataModel.getClose ();
            i++;
        }
        OHLC ohlc = null;
        try {
            ohlc = new OHLC (symbol, period, dates, open, high, low, close);
        } catch ( CorruptedMarketDataException e ) {
            assert(false);
        }
        return ohlc;
    }

    public MarketData getOHLC (Symbol symbol, Period period, Date startDate) {
        return getOHLC (symbol, period, startDate, new Date());
    }

    private void initializeHistorical(){
        String folderName = conf.getString (ConfKey.CSV_DIR);
        File folder = new File(folderName);
        initializeCsvFolder (folder);
    }

    private void initializeCsvFolder(File folder){
        try {
            log.info ("Loading history data from directory {}",folder.getCanonicalPath ());
            File[] listOfFiles = folder.listFiles();
            if (listOfFiles != null) {
                for ( File file : listOfFiles ) {
                    initializeCsvFile (file);
                }
            }else{
                log.info ("No file in {}", folder.getCanonicalPath ());
            }
        } catch ( IOException e ) {
            log.error ("Csv folder initialization error", e);
        }
    }

    private void initializeCsvFile(File file){
        if (file.isFile()) {
            try {
                cacheFromCsv (file);
            }catch ( IllegalArgumentException ex ){
                log.error ("Illegal file format", ex);
            }catch ( Exception ex ){
                log.error ("Csv load error at file "+file.getName (), ex);
            }
        } else if (file.isDirectory()) {
            initializeCsvFolder (file);
        }
    }

    private enum HeaderDef{
        DATE("DATE", "date", "D", "d"),
        OPEN("OPEN", "open", "O", "o"),
        HIGH("HIGH", "high", "H", "h"),
        LOW("LOW","low","L","l"),
        CLOSE("CLOSE","close","C","c"),
        VOLUME("VOLUME","volume","V","v")
        ;

        private String[] defs;

        HeaderDef (String... defs) {
            this.defs = defs;
        }

        public boolean matches(String value){
            for (String def : defs){
                if (def.equals (value)){
                    return true;
                }
            }
            return false;
        }
    }

    private void cacheFromCsv(File csvFile){
        BufferedReader br = null;
        try {
            List<Date> dateList = new ArrayList<Date> ();
            List<Double> openList = new ArrayList<Double> ();
            List<Double> highList = new ArrayList<Double> ();
            List<Double> lowList = new ArrayList<Double> ();
            List<Double> closeList = new ArrayList<Double> ();

            Symbol symbol = null;
            Period period = null;
            String dateFormat = null;
            int dateIndex = - 1, openIndex = -1, highIndex = -1, lowIndex = -1, closeIndex = -1, volumeIndex = -1;

            String currentLine;
            br = new BufferedReader(new FileReader (csvFile));
            int i = 0;
            while ((currentLine = br.readLine()) != null) {
                if (i == 0){
                    String[] symbolPeriodPair = currentLine.split (",");
                    if (symbolPeriodPair.length != 2){
                        throw new IllegalArgumentException ();
                    }else{
                        symbol = Symbols.of (symbolPeriodPair[0]);
                        period = Period.valueOf (symbolPeriodPair[1]);
                        if (symbol == null || period == null){
                            throw new IllegalArgumentException ();
                        }
                    }
                }else if (i == 1){
                    String[] columns = currentLine.split (",");
                    int cIdx = 0;
                    for (String column : columns){
                        String[] columnDefPair = column.split ("#");
                        if ( HeaderDef.DATE.matches (columnDefPair[0])){
                            dateIndex = cIdx;
                            if (columnDefPair.length > 1){
                                dateFormat = columnDefPair[1];
                            }
                        }else if ( HeaderDef.OPEN.matches (columnDefPair[0])){
                            openIndex = cIdx;
                        }else if ( HeaderDef.HIGH.matches (columnDefPair[0])){
                            highIndex = cIdx;
                        }else if ( HeaderDef.LOW.matches (columnDefPair[0])){
                            lowIndex = cIdx;
                        }else if ( HeaderDef.CLOSE.matches (columnDefPair[0])){
                            closeIndex = cIdx;
                        }else if ( HeaderDef.VOLUME.matches (columnDefPair[0])){
                            volumeIndex = cIdx;
                        }
                        cIdx++;
                    }
                }else{
                    Date date = null;
                    double open = 0, high = 0, low = 0, close = 0, volume = 0;
                    String[] values = currentLine.split (",");
                    if (dateIndex > -1 && dateIndex < values.length){
                        date = new SimpleDateFormat (dateFormat).parse (values[dateIndex]);
                    }
                    if (openIndex > -1 && openIndex < values.length){
                        open = Double.valueOf (values[openIndex]);
                    }
                    if (highIndex > -1 && highIndex < values.length){
                        high = Double.valueOf (values[highIndex]);
                    }
                    if (lowIndex > -1 && lowIndex < values.length){
                        low = Double.valueOf (values[lowIndex]);
                    }
                    if (closeIndex > -1 && closeIndex < values.length){
                        close = Double.valueOf (values[closeIndex]);
                    }
                    if (volumeIndex > -1 && volumeIndex < values.length){
                        volume = Double.valueOf (values[volumeIndex]);
                    }
                    dateList.add (date);
                    openList.add (open);
                    highList.add (high);
                    lowList.add (low);
                    closeList.add (close);
                }
                i++;
            }
            ohlcList.add (new OHLC (symbol, period, dateList,
                                    NumberUtils.toArray (openList),
                                    NumberUtils.toArray (highList),
                                    NumberUtils.toArray (openList),
                                    NumberUtils.toArray (closeList)
                                    )
            );
            log.info ("Created new base series ({}, {}) from file {}", symbol, period, csvFile.getName ());
        } catch (IOException e) {
            log.error ("IO exception on file", e);
        } catch ( ParseException e ) {
            log.error ("Parse exception", e);
        } catch ( CorruptedMarketDataException e ) {
            log.error (e.getMessage ());
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                log.error ("Close error", ex);
            }
        }
    }

}
