package com.ayronasystems.core.service;

import com.ayronasystems.core.configuration.ConfKey;
import com.ayronasystems.core.configuration.Configuration;
import com.ayronasystems.core.dao.mysql.Tick4JDao;
import com.ayronasystems.core.dao.mysql.Tick4JDaoImpl;
import com.ayronasystems.core.dao.mysql.model.BarModel;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.ayronasystems.core.timeseries.moment.Moment;
import com.ayronasystems.core.timeseries.moment.Tick;
import com.ayronasystems.core.timeseries.series.SeriesIterator;
import com.ayronasystems.core.timeseries.series.SymbolTimeSeries;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by gorkemgok on 12/07/15.
 */
public class SymbolSeriesManager<M extends Moment> {

    private static Logger log = LoggerFactory.getLogger (SymbolSeriesManager.class);

    private static Configuration conf = Configuration.getInstance ();

    private static SymbolSeriesManager<Bar> barSymbolSeriesManager = null;

    private static SymbolSeriesManager<Tick> tickSymbolSeriesManager = null;

    private Map<Interval, SymbolTimeSeries<M>> seriesMap = new HashMap<Interval, SymbolTimeSeries<M>> ();

    private Tick4JDao dao = Tick4JDaoImpl.getDao ();

    private final Class<M> classOfMoment;

    //TODO:initialize symbolseriesmanager at startup
    private SymbolSeriesManager (Class<M> classOfMoment) {
        this.classOfMoment = classOfMoment;
        initializeHistorical ();
    }

    public static SymbolSeriesManager getManager (Class<? extends Moment> momentClass) {
        if ( momentClass.equals (Bar.class) ) {
            if ( barSymbolSeriesManager == null ) {
                barSymbolSeriesManager = new SymbolSeriesManager<Bar> (Bar.class);
            }

            return barSymbolSeriesManager;
        } else if ( momentClass.equals (Tick.class) ) {
            if ( tickSymbolSeriesManager == null ) {
                tickSymbolSeriesManager = new SymbolSeriesManager<Tick> (Tick.class);
            }

            return tickSymbolSeriesManager;
        } else {
            throw new NoSuchElementException (String.format ("No Manager for Moment : %s", momentClass.getName ()));
        }
    }

    public SymbolTimeSeries<M> get (Symbol symbol, Period period, Date beginDate, Date endDate) {
        Interval interval = new Interval (beginDate.getTime (), endDate.getTime ());
        for (Interval seriesInterval: seriesMap.keySet ()) {
            SymbolTimeSeries series = seriesMap.get (seriesInterval);
            if ( series.getPeriod () == period &&
                    series.getSymbol () == symbol &&
                    series.size () > 0 ) {
                if ( seriesInterval.equals (interval) ) {
                    return series;
                } else if ( seriesInterval.contains (interval) ) {
                    SymbolTimeSeries<M> subSeries = new SymbolTimeSeries (symbol, period, classOfMoment);
                    SeriesIterator<SymbolTimeSeries<M>,M> iterator = series.begin ();
                    while ( iterator.hasNext () ){
                        M moment = iterator.next ();
                        if ((moment.getDate ().after (beginDate) || moment.getDate ().equals (beginDate))
                                && (moment.getDate ().before (endDate) || moment.getDate ().equals (endDate))){
                            subSeries.addMoment (moment);
                        }
                    }
                    seriesMap.put (interval, subSeries);
                    return subSeries;
                }
            }
        }
        return createSymbolSeries (symbol, period, beginDate, endDate);
    }

    private SymbolTimeSeries createSymbolSeries (Symbol symbol, Period period, Date beginDate, Date endDate) {
        if (Bar.class.equals(classOfMoment)) {
            List<BarModel> barModels = dao.findBarsBetweenDates (symbol, period, beginDate, endDate);
            SymbolTimeSeries symbolTimeSeries = createBarSymbolSeries (symbol, period, barModels);
            seriesMap.put (new Interval (beginDate.getTime (), endDate.getTime ()), symbolTimeSeries);
            return symbolTimeSeries;
        }else if (Tick.class.equals(classOfMoment)) {
            //TODO: create tick moment model and manager
        }
        return null;
    }

    public SymbolTimeSeries<M> get (Symbol symbol, Period period) {
        for ( SymbolTimeSeries series : seriesMap.values () ) {
            if (       series.getPeriod () == period
                    && series.getSymbol () == symbol){
                return series;
            }
        }
        return createSymbolSeries (symbol, period);
    }

    private SymbolTimeSeries createSymbolSeries (Symbol symbol, Period period) {
        if (Bar.class.equals(classOfMoment)) {
            List<BarModel> barModels = dao.findBars (symbol, period);
            SymbolTimeSeries symbolTimeSeries = createBarSymbolSeries (symbol, period, barModels);
            seriesMap.put (new Interval (symbolTimeSeries.getBeginningDate ().getTime (), symbolTimeSeries.getEndingDate ().getTime ()), symbolTimeSeries);
            return symbolTimeSeries;
        }else if (Bar.class.equals(classOfMoment)) {
            //TODO: create tick moment model and manager
        }
        return null;
    }

    private SymbolTimeSeries createBarSymbolSeries (Symbol symbol, Period period, List<BarModel> barModels) {
        SymbolTimeSeries<M> newSymbolSeries = new SymbolTimeSeries<M>(symbol, period, classOfMoment);
        for (BarModel barModel : barModels) {
            newSymbolSeries.addMoment((M) barModel.convertToMoment());
        }
        return newSymbolSeries;
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
            SymbolTimeSeries<M> newSymbolSeries = null;
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
                        symbol = Symbol.valueOf (symbolPeriodPair[0]);
                        period = Period.valueOf (symbolPeriodPair[1]);
                        if (symbol == null || period == null){
                            throw new IllegalArgumentException ();
                        }
                        newSymbolSeries = new SymbolTimeSeries(symbol, period, Bar.class);
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
                    M bar = (M)new Bar (date,open,high,low,close,volume);

                    newSymbolSeries.addMoment(bar);
                }
                i++;
            }
            seriesMap.put (new Interval (newSymbolSeries.getBeginningDate ().getTime (), newSymbolSeries.getEndingDate ().getTime ()), newSymbolSeries);
            log.info ("Created new base series ({}, {}) from file {}", symbol, period, csvFile.getName ());
        } catch (IOException e) {
            log.error ("IO exception on file", e);
        } catch ( ParseException e ) {
            log.error ("Parse exception", e);
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                log.error ("Close error", ex);
            }
        }
    }

}
