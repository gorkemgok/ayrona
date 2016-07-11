package com.ayronasystems.core.data;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbols;
import com.ayronasystems.core.definition.SymbolPeriod;
import com.ayronasystems.core.exception.CorruptedMarketDataException;
import com.ayronasystems.core.util.DateUtils;
import com.ayronasystems.core.util.Interval;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by gorkemgok on 12/06/16.
 */
public class MarketDataCacheTest {

    private MarketDataCache marketDataCache;

    private OHLC ohlc;

    private OHLC ohlc2;

    @Before
    public void setUp () throws Exception {
        List<Date> dates = new ArrayList<Date> (Arrays.asList (new Date[]{
                DateUtils.parseDate ("01.01.2016 01:00:00"), DateUtils.parseDate ("01.01.2016 01:05:00"), DateUtils.parseDate ("01.01.2016 01:10:00"),
                DateUtils.parseDate ("01.01.2016 01:15:00"), DateUtils.parseDate ("01.01.2016 01:20:00"), DateUtils.parseDate ("01.01.2016 01:25:00")
        }));

        double[] os = new double[]{0,1,2,3,4,5};
        double[] hs = new double[]{0,1,2,3,4,5};
        double[] ls = new double[]{0,1,2,3,4,5};
        double[] cs = new double[]{0,1,2,3,4,5};

        ohlc = new OHLC (Symbols.of("TEST"), Period.M5, dates, os, hs, ls, cs);
        marketDataCache = new MarketDataCache ();
        marketDataCache.cache (ohlc);

        List<Date> dates2 = new ArrayList<Date> (Arrays.asList (new Date[]{
                DateUtils.parseDate ("01.01.2016 00:40:00"),
                DateUtils.parseDate ("01.01.2016 00:45:00"), DateUtils.parseDate ("01.01.2016 00:50:00"), DateUtils.parseDate ("01.01.2016 00:55:00"),
                DateUtils.parseDate ("01.01.2016 01:00:00"), DateUtils.parseDate ("01.01.2016 01:05:00"), DateUtils.parseDate ("01.01.2016 01:10:00"),
                DateUtils.parseDate ("01.01.2016 01:15:00"), DateUtils.parseDate ("01.01.2016 01:20:00"), DateUtils.parseDate ("01.01.2016 01:25:00"),
                DateUtils.parseDate ("01.01.2016 01:30:00")
        }));

        double[] os2 = new double[]{0,1,2,3,4,5,6,7,8,9,10};
        double[] hs2 = new double[]{0,1,2,3,4,5,6,7,8,9,10};
        double[] ls2 = new double[]{0,1,2,3,4,5,6,7,8,9,10};
        double[] cs2 = new double[]{0,1,2,3,4,5,6,7,8,9,10};

        ohlc2 = new OHLC (Symbols.of("TEST"), Period.M5, dates2, os2, hs2, ls2, cs2);

    }

    @Test
    public void cache() throws CorruptedMarketDataException {
        List<Date> dates = new ArrayList<Date> (Arrays.asList (new Date[]{
                DateUtils.parseDate ("01.01.2016 00:45:00"), DateUtils.parseDate ("01.01.2016 00:50:00"), DateUtils.parseDate ("01.01.2016 00:55:00")
        }));
        double[] os = new double[]{3.2,4.2,5.2};
        double[] hs = new double[]{3.21,4.21,5.21};
        double[] ls = new double[]{3.22,4.22,5.22};
        double[] cs = new double[]{3.23,4.23,5.23};

        ohlc = new OHLC (Symbols.of("TEST"), Period.M5, dates, os, hs, ls, cs);
        marketDataCache.cache(ohlc);

        MarketDataCacheResult marketDataCacheResult = marketDataCache.search(
                new SymbolPeriod(Symbols.of("TEST"), Period.M5),
                new Interval(DateUtils.parseDate ("01.01.2016 00:45:00"), DateUtils.parseDate ("01.01.2016 01:30:00"))
        );

        assertTrue(marketDataCacheResult.hasData());

        MarketData marketData = marketDataCacheResult.getFoundData();

        assertEquals(9, marketData.size());

        assertEquals(DateUtils.parseDate ("01.01.2016 00:45:00"), marketData.getBeginningDate());
        assertEquals(DateUtils.parseDate ("01.01.2016 01:25:00"), marketData.getLastDate());
        assertEquals(DateUtils.parseDate ("01.01.2016 01:30:00"), marketData.getEndingDate());

        marketDataCache.cache(ohlc2);

        marketDataCacheResult = marketDataCache.search(
                new SymbolPeriod(Symbols.of("TEST"), Period.M5),
                new Interval(DateUtils.parseDate ("01.01.2016 00:40:00"), DateUtils.parseDate ("01.01.2016 01:35:00"))
        );

        assertTrue(marketDataCacheResult.hasData());

        marketData = marketDataCacheResult.getFoundData();

        assertEquals(11, marketData.size());

        assertEquals(DateUtils.parseDate ("01.01.2016 00:40:00"), marketData.getBeginningDate());
        assertEquals(DateUtils.parseDate ("01.01.2016 01:30:00"), marketData.getLastDate());
        assertEquals(DateUtils.parseDate ("01.01.2016 01:35:00"), marketData.getEndingDate());
    }

    @Test
    public void search () throws Exception {
        SymbolPeriod symbolPeriod = new SymbolPeriod (Symbols.of("TEST"), Period.M5);

        MarketDataCacheResult r0 = marketDataCache.search(symbolPeriod,
                                                        new Interval (
                                                                DateUtils.parseDate ("01.01.2016 01:05:00"),
                                                                DateUtils.parseDate ("01.01.2016 01:25:00")
                                                        ));

        assertTrue (r0.hasData());
        assertTrue (r0.getAbsentIntervals ().isEmpty ());
        assertEquals (4, r0.getFoundData ().size ());
        assertEquals (ohlc.getDates ().get (1), r0.getFoundData ().getDates ().get (0));
        assertEquals (ohlc.getDates ().get (2), r0.getFoundData ().getDates ().get (1));
        assertEquals (ohlc.getDates ().get (3), r0.getFoundData ().getDates ().get (2));
        assertEquals (ohlc.getDates ().get (4), r0.getFoundData ().getDates ().get (3));

        MarketDataCacheResult r1 = marketDataCache.search(symbolPeriod,
                                                        new Interval (
                                                                DateUtils.parseDate ("01.01.2016 01:00:00"),
                                                                DateUtils.parseDate ("01.01.2016 01:30:00")
                                                        ));

        assertTrue (r1.hasData());
        assertTrue (r1.getAbsentIntervals ().isEmpty ());
        assertEquals (6, r1.getFoundData ().size ());
        assertEquals (ohlc.getDates ().get (0), r1.getFoundData ().getDates ().get (0));
        assertEquals (ohlc.getDates ().get (1), r1.getFoundData ().getDates ().get (1));
        assertEquals (ohlc.getDates ().get (2), r1.getFoundData ().getDates ().get (2));
        assertEquals (ohlc.getDates ().get (3), r1.getFoundData ().getDates ().get (3));
        assertEquals (ohlc.getDates ().get (4), r1.getFoundData ().getDates ().get (4));
        assertEquals (ohlc.getDates ().get (5), r1.getFoundData ().getDates ().get (5));

        MarketDataCacheResult r2 = marketDataCache.search(symbolPeriod,
                                                        new Interval (
                                                                DateUtils.parseDate ("01.01.2016 01:05:00"),
                                                                DateUtils.parseDate ("01.01.2016 01:30:00")
                                                        ));

        assertTrue (r2.hasData());
        assertTrue (r2.getAbsentIntervals ().isEmpty ());
        assertEquals (5, r2.getFoundData ().size ());
        assertEquals (ohlc.getDates ().get (1), r2.getFoundData ().getDates ().get (0));
        assertEquals (ohlc.getDates ().get (2), r2.getFoundData ().getDates ().get (1));
        assertEquals (ohlc.getDates ().get (3), r2.getFoundData ().getDates ().get (2));
        assertEquals (ohlc.getDates ().get (4), r2.getFoundData ().getDates ().get (3));
        assertEquals (ohlc.getDates ().get (5), r2.getFoundData ().getDates ().get (4));

        MarketDataCacheResult r3 = marketDataCache.search(symbolPeriod,
                                                        new Interval (
                                                                DateUtils.parseDate ("01.01.2016 01:00:00"),
                                                                DateUtils.parseDate ("01.01.2016 01:25:00")
                                                        ));

        assertTrue (r3.hasData());
        assertTrue (r3.getAbsentIntervals ().isEmpty ());
        assertEquals (5, r3.getFoundData ().size ());
        assertEquals (ohlc.getDates ().get (0), r3.getFoundData ().getDates ().get (0));
        assertEquals (ohlc.getDates ().get (1), r3.getFoundData ().getDates ().get (1));
        assertEquals (ohlc.getDates ().get (2), r3.getFoundData ().getDates ().get (2));
        assertEquals (ohlc.getDates ().get (3), r3.getFoundData ().getDates ().get (3));
        assertEquals (ohlc.getDates ().get (4), r3.getFoundData ().getDates ().get (4));

        MarketDataCacheResult r4 = marketDataCache.search(symbolPeriod,
                                                        new Interval (
                                                                DateUtils.parseDate ("01.01.2016 00:50:00"),
                                                                DateUtils.parseDate ("01.01.2016 01:15:00")
                                                        ));

        assertTrue (r4.hasData());
        assertEquals (1, r4.getAbsentIntervals ().size ());
        assertEquals (DateUtils.parseDate ("01.01.2016 00:50:00"),
                      r4.getAbsentIntervals ().get (0).getBeginningDate ());
        assertEquals (DateUtils.parseDate ("01.01.2016 01:00:00"),
                      r4.getAbsentIntervals ().get (0).getEndingDate ());
        assertEquals (3, r4.getFoundData ().size ());
        assertEquals (ohlc.getDates ().get (0), r4.getFoundData ().getDates ().get (0));
        assertEquals (ohlc.getDates ().get (1), r4.getFoundData ().getDates ().get (1));
        assertEquals (ohlc.getDates ().get (2), r4.getFoundData ().getDates ().get (2));

        MarketDataCacheResult r5 = marketDataCache.search(symbolPeriod,
                                                        new Interval (
                                                                DateUtils.parseDate ("01.01.2016 01:15:00"),
                                                                DateUtils.parseDate ("01.01.2016 01:50:00")
                                                        ));

        assertTrue (r5.hasData());
        assertEquals (1, r5.getAbsentIntervals ().size ());
        assertEquals (DateUtils.parseDate ("01.01.2016 01:30:00"),
                      r5.getAbsentIntervals ().get (0).getBeginningDate ());
        assertEquals (DateUtils.parseDate ("01.01.2016 01:50:00"),
                      r5.getAbsentIntervals ().get (0).getEndingDate ());
        assertEquals (3, r5.getFoundData ().size ());
        assertEquals (ohlc.getDates ().get (3), r5.getFoundData ().getDates ().get (0));
        assertEquals (ohlc.getDates ().get (4), r5.getFoundData ().getDates ().get (1));
        assertEquals (ohlc.getDates ().get (5), r5.getFoundData ().getDates ().get (2));

        MarketDataCacheResult r6 = marketDataCache.search(symbolPeriod,
                                                        new Interval (
                                                                DateUtils.parseDate ("01.01.2016 00:50:00"),
                                                                DateUtils.parseDate ("01.01.2016 01:30:00")
                                                        ));

        assertTrue (r6.hasData());
        assertEquals (1, r6.getAbsentIntervals ().size ());
        assertEquals (DateUtils.parseDate ("01.01.2016 00:50:00"),
                      r6.getAbsentIntervals ().get (0).getBeginningDate ());
        assertEquals (DateUtils.parseDate ("01.01.2016 01:00:00"),
                      r6.getAbsentIntervals ().get (0).getEndingDate ());
        assertEquals (6, r6.getFoundData ().size ());
        assertEquals (ohlc.getDates ().get (0), r6.getFoundData ().getDates ().get (0));
        assertEquals (ohlc.getDates ().get (1), r6.getFoundData ().getDates ().get (1));
        assertEquals (ohlc.getDates ().get (2), r6.getFoundData ().getDates ().get (2));
        assertEquals (ohlc.getDates ().get (3), r6.getFoundData ().getDates ().get (3));
        assertEquals (ohlc.getDates ().get (4), r6.getFoundData ().getDates ().get (4));
        assertEquals (ohlc.getDates ().get (5), r6.getFoundData ().getDates ().get (5));

    }

}