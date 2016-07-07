package com.ayronasystems.core.data;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.exception.CorruptedMarketDataException;
import com.ayronasystems.core.exception.MarketDataConversionException;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.ayronasystems.core.timeseries.moment.ColumnDefinition;
import com.ayronasystems.core.timeseries.moment.Moment;
import com.ayronasystems.core.util.Interval;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by gorkemgok on 19/03/16.
 */
public class OHLC implements MarketData {

    public class OHLCIterator implements Iterator<Moment>{

        private int i = 0;

        private int size;

        public OHLCIterator () {
            this.size = dates.size ();
        }

        public boolean hasNext () {
            return i < size;
        }

        public Moment next () {
            Moment moment = new Bar (dates.get (i), openSeries[i], highSeries[i], lowSeries[i], closeSeries[i], 0);
            i++;
            return moment;
        }

        public void remove () {
            throw new NotImplementedException ();
        }
    }

    private Symbol symbol;

    private Period period;

    private Interval interval;

    protected List<Date> dates;

    protected double[] openSeries;

    protected double[] highSeries;

    protected double[] lowSeries;

    protected double[] closeSeries;

    //Test purpose only
    OHLC (double[] openSeries, double[] highSeries, double[] lowSeries, double[] closeSeries) {
        this.openSeries = openSeries;
        this.highSeries = highSeries;
        this.lowSeries = lowSeries;
        this.closeSeries = closeSeries;
    }

    public OHLC(Symbol symbol, Period period, List<Bar> barList) throws CorruptedMarketDataException {
        int size = barList.size ();
        List<Date> dates = new ArrayList<Date> ();
        double[] openSeries = new double[size];
        double[] highSeries = new double[size];
        double[] lowSeries = new double[size];
        double[] closeSeries = new double[size];
        int i = 0;
        for (Bar bar : barList){
            dates.add (bar.getDate ());
            openSeries[i] = bar.getOpen ();
            highSeries[i] = bar.getHigh ();
            lowSeries[i] = bar.getLow ();
            closeSeries[i] = bar.getClose ();
            i++;
        }
        this.symbol = symbol;
        this.period = period;
        this.dates = dates;
        if (dates != null && !dates.isEmpty ()) {
            this.interval = new Interval (dates.get (0), new Date (dates.get (dates.size () - 1)
                                                                        .getTime () + period.getAsMillis ()));
        }else{
            this.interval = Interval.ZERO;
        }
        this.openSeries = openSeries;
        this.highSeries = highSeries;
        this.lowSeries = lowSeries;
        this.closeSeries = closeSeries;
        if (dates.size () != openSeries.length ||
                openSeries.length != highSeries.length ||
                highSeries.length != lowSeries.length ||
                lowSeries.length != closeSeries.length){
            // TODO: enter message
            throw new CorruptedMarketDataException ();
        }
    }

    public OHLC (Symbol symbol, Period period, List<Date> dates, double[] openSeries, double[] highSeries, double[] lowSeries, double[] closeSeries) throws CorruptedMarketDataException {
        this.symbol = symbol;
        this.period = period;
        this.dates = dates;
        if (dates != null && !dates.isEmpty ()) {
            this.interval = new Interval (dates.get (0), new Date (dates.get (dates.size () - 1)
                                                                        .getTime () + period.getAsMillis ()));
        }else{
            this.interval = Interval.ZERO;
        }
        this.openSeries = openSeries;
        this.highSeries = highSeries;
        this.lowSeries = lowSeries;
        this.closeSeries = closeSeries;
        if (dates.size () != openSeries.length ||
            openSeries.length != highSeries.length ||
                highSeries.length != lowSeries.length ||
                    lowSeries.length != closeSeries.length){
            // TODO: enter message
            throw new CorruptedMarketDataException ();
        }
    }

    public Symbol getSymbol () {
        return symbol;
    }

    public Period getPeriod () {
        return period;
    }

    public Date getBeginningDate () {
        return interval.getBeginningDate ();
    }

    public Date getEndingDate () {
        return interval.getEndingDate ();
    }

    public Date getLastDate() {
        return dates.get( dates.size() - 1 );
    }

    public double getPrice (PriceColumn priceColumn, int index) {
        switch ( priceColumn ){
            case OPEN:
                return openSeries[index];
            case HIGH:
                return highSeries[index];
            case LOW:
                return lowSeries[index];
            case CLOSE:
                return closeSeries[index];
            default:
                throw new IllegalArgumentException ("Illegal OHLC column");
        }
    }

    public Date getDate(int index){
        return dates.get (index);
    }

    public MarketData subData (int period) {
        if (period <= openSeries.length) {
            int beginIdx = openSeries.length - period;
            int endIdx = openSeries.length - 1;
            return subData(beginIdx, endIdx);
        }else{
            return OHLC.getEmptyData(this.symbol, this.period);
        }
    }

    public MarketData subData (int beginIdx, int endIdx) {
        if (beginIdx < 0 || endIdx < 0 || endIdx > openSeries.length){
            throw new IndexOutOfBoundsException ("Market Data does not contains the interval");
        }
        int size = endIdx - beginIdx + 1;
        double[] newOpenSeries = new double[size];
        double[] newHighSeries = new double[size];
        double[] newLowSeries = new double[size];
        double[] newCloseSeries = new double[size];
        List<Date> newDates = new ArrayList<Date> (size);
        int j = beginIdx;
        for ( int i = 0; i < size; i++ ) {
            newOpenSeries[i] = openSeries[j];
            newHighSeries[i] = highSeries[j];
            newLowSeries[i] = lowSeries[j];
            newCloseSeries[i] = closeSeries[j];
            newDates.add (dates.get (j));
            j++;
        }
        OHLC ohlc = null;
        try {
            ohlc = new OHLC (getSymbol (),
                                             getPeriod (),
                                             newDates,
                                             newOpenSeries,
                                             newHighSeries,
                                             newLowSeries,
                                             newCloseSeries);
        } catch ( CorruptedMarketDataException e ) {
            assert (false);
        }
        return ohlc;
    }

    public MarketData subData (Date beginDate, Date endDate) {
        endDate = new Date(endDate.getTime () - period.getAsMillis ());
        int beginIdx = -1;
        int endIdx = -1;
        int i = 0;
        for (Date date : dates){
            if (beginIdx == -1 && (date.equals (beginDate) || date.after (beginDate))){
                beginIdx = i;
            }
            if (endIdx == -1){
                if (date.equals (endDate)) {
                    endIdx = i;
                    break;
                }else if (date.after (endDate)){
                    endIdx = i - 1;
                    break;
                }
            }
            i++;
        }
        return subData (beginIdx, endIdx);
    }

    public MarketData convert (Period period) throws MarketDataConversionException{
        if (this.period.canDivide (period)){
            long periodTime = period.getAsMillis ();
            List<Bar> newBarList = new ArrayList<Bar> ();
            long periodDate = 0;
            long currentPeriodDate = 0;
            double open = 0;
            double high = 0;
            double low = Double.MAX_VALUE;
            double close = 0;
            boolean justStarted = true;
            for (Moment moment : this){
                currentPeriodDate = moment.getDate ()
                                   .getTime () - (moment.getDate ()
                                                        .getTime () % periodTime);
                if (!justStarted && currentPeriodDate != periodDate){
                    Bar bar = new Bar (new Date(periodDate), open, high, low, close , 0);
                    newBarList.add (bar);
                    periodDate = moment.getDate ().getTime () - (moment.getDate ().getTime () % periodTime);
                    open = moment.get (ColumnDefinition.OPEN);
                    high = moment.get (ColumnDefinition.HIGH);
                    low = moment.get (ColumnDefinition.LOW);
                }
                if (justStarted) {
                    periodDate = moment.getDate ()
                                       .getTime () - (moment.getDate ()
                                                            .getTime () % periodTime);
                    open = moment.get (ColumnDefinition.OPEN);
                }
                high = Math.max (moment.get (ColumnDefinition.HIGH), high);
                low = Math.min (moment.get (ColumnDefinition.LOW), low);
                close = moment.get (ColumnDefinition.CLOSE);
                justStarted = false;
            }
            try {
                return new OHLC (symbol, period, newBarList);
            } catch ( CorruptedMarketDataException e ) {
                assert false;
            }
        }
        throw new MarketDataConversionException (this.period, period);
    }

    public MarketData append(MarketData marketData) {
        int count = marketData.size () + size ();
        List<Date> newDates = new ArrayList<Date> (count);
        double[] newOpenSeries = new double[count];
        double[] newHighSeries = new double[count];
        double[] newLowSeries = new double[count];
        double[] newCloseSeries = new double[count];
        int c = 0;
        for ( int i = 0; i < size (); i++ ) {
            newDates.add (getDate (i));
            newOpenSeries[c] = getOpen (i);
            newHighSeries[c] = getHigh (i);
            newLowSeries[c] = getLow (i);
            newCloseSeries[c] = getClose (i);
            c++;
        }
        for ( int i = 0; i < marketData.size (); i++ ) {
            newDates.add (marketData.getDate (i));
            newOpenSeries[c] = marketData.getPrice (PriceColumn.OPEN, i);
            newHighSeries[c] = marketData.getPrice (PriceColumn.HIGH, i);
            newLowSeries[c] = marketData.getPrice (PriceColumn.LOW, i);
            newCloseSeries[c] = marketData.getPrice (PriceColumn.CLOSE, i);
            c++;
        }

        try {
            return new OHLC (symbol, period, newDates, newOpenSeries, newHighSeries, newLowSeries, newCloseSeries);
        } catch ( CorruptedMarketDataException e ) {
            return this;
        }
    }

    public MarketData merge(MarketData marketData) {
        if (marketData.isEmpty ()){
            return this;
        }else if (isEmpty ()){
            return marketData;
        }else if (marketData.getBeginningDate ().equals (getEndingDate ()) ||
                marketData.getBeginningDate ().after (getEndingDate ())){
            return this.append(marketData);
        }else if(marketData.getLastDate ().equals (getBeginningDate ()) ||
                marketData.getLastDate ().before (getBeginningDate ())){
            return marketData.append(this);
        }else if (marketData.getInterval().contains(getInterval())){
            return marketData;
        }else{
            return this;
        }
    }

    public MarketData safeMerge(MarketData marketData) {
        if (marketData.getBeginningDate ().equals (getEndingDate ())){
            return this.append(marketData);
        }else if(marketData.getEndingDate ().equals (getBeginningDate ())){
            return marketData.append(this);
        }else if (marketData.getInterval().contains(getInterval())){
            return marketData;
        }else{
            return this;
        }
    }

    public double getOpen(int index){
        return openSeries[index];
    }

    public double getHigh(int index){
        return highSeries[index];
    }

    public double getLow(int index){
        return lowSeries[index];
    }

    public double getClose(int index){
        return closeSeries[index];
    }

    public double[] getOpenSeries () {
        return openSeries;
    }

    public double[] getHighSeries () {
        return highSeries;
    }

    public double[] getLowSeries () {
        return lowSeries;
    }

    public double[] getCloseSeries () {
        return closeSeries;
    }

    public List<Date> getDates () {
        return new ArrayList<Date> (dates);
    }

    public Interval getInterval () {
        return interval;
    }

    public int size () {
        return dates.size ();
    }

    public boolean isEmpty () {
        return dates.isEmpty ();
    }

    public double[] getPrice (PriceColumn priceColumn) {
        switch ( priceColumn ){
            case OPEN:
                return getOpenSeries ().clone ();
            case HIGH:
                return getHighSeries ().clone ();
            case LOW:
                return getLowSeries ().clone ();
            case CLOSE:
                return getCloseSeries ().clone ();
            default:
                throw new IllegalArgumentException ("Illegal OHLC column");
        }
    }

    public Iterator<Moment> iterator () {
        return new OHLCIterator ();
    }

    public static OHLC getEmptyData(Symbol symbol, Period period){
        double[] emptyArr = new double[0];
        OHLC ohlc = null;
        try {
            ohlc = new OHLC (symbol, period, new ArrayList<Date> (), emptyArr, emptyArr.clone(), emptyArr.clone(), emptyArr.clone());
        } catch ( CorruptedMarketDataException e ) {
            e.printStackTrace ();
        }
        return ohlc;
    }
}
