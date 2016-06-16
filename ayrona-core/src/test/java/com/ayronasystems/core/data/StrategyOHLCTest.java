package com.ayronasystems.core.data;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.timeseries.moment.Bar;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static com.ayronasystems.core.data.TestData.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by gorkemgok on 28/05/16.
 */
public class StrategyOHLCTest {

    private MarketData marketData;

    private StrategyOHLC slidingStrategyOHLC;

    private StrategyOHLC growingStrategyOHLC;

    @Before
    public void setUp () throws Exception {
        marketData = new OHLC (null, Period.M5, new ArrayList<Date> (DATES), OPEN_SERIES.clone (), HIGH_SERIES.clone (), LOW_SERIES.clone (), CLOSE_SERIES.clone ());
        slidingStrategyOHLC = new SlidingStrategyOHLC(null, Period.M5, new ArrayList<Date> (DATES), OPEN_SERIES.clone (), HIGH_SERIES.clone (), LOW_SERIES.clone (), CLOSE_SERIES.clone ());
        growingStrategyOHLC = new GrowingStrategyOHLC (null, Period.M5, new ArrayList<Date> (DATES), OPEN_SERIES.clone (), HIGH_SERIES.clone (), LOW_SERIES.clone (), CLOSE_SERIES.clone ());
    }

    @Test
    public void addNewBarToSlidingOHLC () throws Exception {
        Bar bar = new Bar (new Date (), 10, 20, 30, 40, 0);
        slidingStrategyOHLC.addNewBar (bar);

        double[] openSeries = slidingStrategyOHLC.getPrice (PriceColumn.OPEN);
        double[] highSeries = slidingStrategyOHLC.getPrice (PriceColumn.HIGH);
        double[] lowSeries = slidingStrategyOHLC.getPrice (PriceColumn.LOW);
        double[] closeSeries = slidingStrategyOHLC.getPrice (PriceColumn.CLOSE);
        assertEquals (OPEN_SERIES.length, openSeries.length);
        assertEquals (HIGH_SERIES.length, highSeries.length);
        assertEquals (LOW_SERIES.length, lowSeries.length);
        assertEquals (CLOSE_SERIES.length, closeSeries.length);

        assertEquals (10, openSeries[openSeries.length - 1], 0);
        assertEquals (OPEN_SERIES[1], openSeries[0], 0);

        assertEquals (20, highSeries[highSeries.length - 1], 0);
        assertEquals (HIGH_SERIES[1], highSeries[0], 0);

        assertEquals (30, lowSeries[lowSeries.length - 1], 0);
        assertEquals (LOW_SERIES[1], lowSeries[0], 0);

        assertEquals (40, closeSeries[closeSeries.length - 1], 0);
        assertEquals (CLOSE_SERIES[1], closeSeries[0], 0);
    }

    @Test
    public void addNewBarToGrowingOHLC () throws Exception {
        Bar bar1 = new Bar (new Date (), 10, 20, 30, 40, 0);
        Bar bar2 = new Bar (new Date (), 11, 21, 31, 41, 1);
        growingStrategyOHLC.addNewBar (bar1);
        growingStrategyOHLC.addNewBar (bar2);

        double[] openSeries = growingStrategyOHLC.getPrice (PriceColumn.OPEN);
        double[] highSeries = growingStrategyOHLC.getPrice (PriceColumn.HIGH);
        double[] lowSeries = growingStrategyOHLC.getPrice (PriceColumn.LOW);
        double[] closeSeries = growingStrategyOHLC.getPrice (PriceColumn.CLOSE);
        assertEquals (OPEN_SERIES.length, openSeries.length - 2);
        assertEquals (HIGH_SERIES.length, highSeries.length - 2);
        assertEquals (LOW_SERIES.length, lowSeries.length - 2);
        assertEquals (CLOSE_SERIES.length, closeSeries.length - 2);

        assertEquals (10, openSeries[openSeries.length - 2], 0);
        assertEquals (11, openSeries[openSeries.length - 1], 0);
        assertEquals (OPEN_SERIES[0], openSeries[0], 0);

        assertEquals (20, highSeries[highSeries.length - 2], 0);
        assertEquals (21, highSeries[highSeries.length - 1], 0);
        assertEquals (HIGH_SERIES[0], highSeries[0], 0);

        assertEquals (30, lowSeries[lowSeries.length - 2], 0);
        assertEquals (31, lowSeries[lowSeries.length - 1], 0);
        assertEquals (LOW_SERIES[0], lowSeries[0], 0);

        assertEquals (40, closeSeries[closeSeries.length - 2], 0);
        assertEquals (41, closeSeries[closeSeries.length - 1], 0);
        assertEquals (CLOSE_SERIES[0], closeSeries[0], 0);
    }

    @Test
    public void createFrom () throws Exception {
        StrategyOHLC strategyOHLC = SlidingStrategyOHLC.valueOf (marketData.subData (3));

        assertEquals (3, strategyOHLC.size ());
        assertEquals (OPEN_SERIES[OPEN_SERIES.length - 1], strategyOHLC.getPrice (PriceColumn.OPEN, strategyOHLC.size () - 1), 0);
        assertEquals (HIGH_SERIES[OPEN_SERIES.length - 1], strategyOHLC.getPrice (PriceColumn.HIGH, strategyOHLC.size () - 1), 0);
        assertEquals (LOW_SERIES[OPEN_SERIES.length - 1], strategyOHLC.getPrice (PriceColumn.LOW, strategyOHLC.size () - 1), 0);
        assertEquals (CLOSE_SERIES[OPEN_SERIES.length - 1], strategyOHLC.getPrice (PriceColumn.CLOSE, strategyOHLC.size () - 1), 0);
    }

}