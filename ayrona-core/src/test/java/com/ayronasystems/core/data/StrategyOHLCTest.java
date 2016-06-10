package com.ayronasystems.core.data;

import static com.ayronasystems.core.data.TestData.*;

import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.timeseries.moment.Bar;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * Created by gorkemgok on 28/05/16.
 */
public class StrategyOHLCTest {

    private MarketData marketData;

    private StrategyOHLC strategyOHLC;

    @Before
    public void setUp () throws Exception {
        marketData = new OHLC (null, null, DATES, OPEN_SERIES, HIGH_SERIES, LOW_SERIES, CLOSE_SERIES);
        strategyOHLC = new SlidingStrategyOHLC(null, null, DATES, OPEN_SERIES, HIGH_SERIES, LOW_SERIES, CLOSE_SERIES);

    }

    @Test
    public void addNewBarToSlidingOHLC () throws Exception {
        Bar bar = new Bar (new Date (), 10, 20, 30, 40, 0);
        strategyOHLC.addNewBar (bar);

        double[] openSeries = strategyOHLC.getData (PriceColumn.OPEN);
        double[] highSeries = strategyOHLC.getData (PriceColumn.HIGH);
        double[] lowSeries = strategyOHLC.getData (PriceColumn.LOW);
        double[] closeSeries = strategyOHLC.getData (PriceColumn.CLOSE);
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
    public void createFrom () throws Exception {
        StrategyOHLC strategyOHLC = SlidingStrategyOHLC.valueOf (marketData.subData (3));

        assertEquals (3, strategyOHLC.getDataCount ());
        assertEquals (OPEN_SERIES[OPEN_SERIES.length - 1], strategyOHLC.getData (PriceColumn.OPEN, strategyOHLC.getDataCount () - 1), 0);
        assertEquals (HIGH_SERIES[OPEN_SERIES.length - 1], strategyOHLC.getData (PriceColumn.HIGH, strategyOHLC.getDataCount () - 1), 0);
        assertEquals (LOW_SERIES[OPEN_SERIES.length - 1], strategyOHLC.getData (PriceColumn.LOW, strategyOHLC.getDataCount () - 1), 0);
        assertEquals (CLOSE_SERIES[OPEN_SERIES.length - 1], strategyOHLC.getData (PriceColumn.CLOSE, strategyOHLC.getDataCount () - 1), 0);
    }

}