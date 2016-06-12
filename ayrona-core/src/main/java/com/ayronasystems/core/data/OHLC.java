package com.ayronasystems.core.data;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.exception.CorruptedMarketDataException;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.ayronasystems.core.timeseries.moment.Moment;
import com.ayronasystems.core.util.Interval;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

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

    public OHLC (Symbol symbol, Period period, List<Date> dates, double[] openSeries, double[] highSeries, double[] lowSeries, double[] closeSeries) throws CorruptedMarketDataException {
        this.symbol = symbol;
        this.period = period;
        this.dates = dates;
        if (dates != null) {
            this.interval = new Interval (dates.get (0), new Date (dates.get (dates.size () - 1)
                                                                        .getTime () + period.getAsMillis ()));
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

    public double getData (PriceColumn priceColumn, int index) {
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
            throw new IndexOutOfBoundsException ();
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
        for (Date date : dates){
            if (beginIdx == -1 && (date.equals (beginDate) || date.after (beginDate))){
                beginIdx = dates.indexOf (beginDate);
            }
            if (endIdx == -1 && (date.equals (endDate) || date.after (beginDate))){
                endIdx = dates.indexOf (endDate);
                break;
            }
        }
        return subData (beginIdx, endIdx);
    }

    public MarketData append (MarketData marketData) {
        OHLC ohlc;
        if (marketData.getBeginningDate ().equals (getEndingDate ())){
            int count = marketData.getDataCount () + getDataCount ();
            List<Date> dates = new ArrayList<Date> (count);
            double[] newOpenSeries = new double[count];
            double[] newHighSeries = new double[count];
            double[] newLowSeries = new double[count];
            double[] newCloseSeries = new double[count];
            int c = 0;
            for ( int i = 0; i < getDataCount () - 1; i++ ) {
                dates.add (getDate (i));
                openSeries[c] = getOpen (i);
                highSeries[c] = getHigh (i);
                lowSeries[c] = getLow (i);
                closeSeries[c] = getClose (i);
            }
            for ( int i = 0; i < marketData.getDataCount () - 1; i++ ) {
                dates.add (marketData.getDate (i));
                openSeries[c] = marketData.getData (PriceColumn.OPEN,i);
                highSeries[c] = marketData.getData (PriceColumn.HIGH,i);
                lowSeries[c] = marketData.getData (PriceColumn.LOW,i);
                closeSeries[c] = marketData.getData (PriceColumn.CLOSE,i);
            }
            try {
                ohlc = new OHLC (symbol, period, dates, openSeries, highSeries, lowSeries, closeSeries);
            } catch ( CorruptedMarketDataException e ) {
                return this;
            }
        }else if(marketData.getEndingDate ().equals (getBeginningDate ())){
            int count = marketData.getDataCount () + getDataCount ();
            List<Date> dates = new ArrayList<Date> (count);
            double[] newOpenSeries = new double[count];
            double[] newHighSeries = new double[count];
            double[] newLowSeries = new double[count];
            double[] newCloseSeries = new double[count];
            int c = 0;
            for ( int i = 0; i < marketData.getDataCount () - 1; i++ ) {
                dates.add (marketData.getDate (i));
                openSeries[c] = marketData.getData (PriceColumn.OPEN,i);
                highSeries[c] = marketData.getData (PriceColumn.HIGH,i);
                lowSeries[c] = marketData.getData (PriceColumn.LOW,i);
                closeSeries[c] = marketData.getData (PriceColumn.CLOSE,i);
            }
            for ( int i = 0; i < getDataCount () - 1; i++ ) {
                dates.add (getDate (i));
                openSeries[c] = getOpen (i);
                highSeries[c] = getHigh (i);
                lowSeries[c] = getLow (i);
                closeSeries[c] = getClose (i);
            }

            try {
                ohlc = new OHLC (symbol, period, dates, openSeries, highSeries, lowSeries, closeSeries);
            } catch ( CorruptedMarketDataException e ) {
                return this;
            }
        }else{
            return this;
        }
        return ohlc;
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
        return dates;
    }

    public Interval getInterval () {
        return interval;
    }

    public int getDataCount () {
        return dates.size ();
    }

    public double[] getData (PriceColumn priceColumn) {
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
            ohlc = new OHLC (symbol, period, Collections.EMPTY_LIST, emptyArr, emptyArr.clone(), emptyArr.clone(), emptyArr.clone());
        } catch ( CorruptedMarketDataException e ) {
            e.printStackTrace ();
        }
        return ohlc;
    }
}
