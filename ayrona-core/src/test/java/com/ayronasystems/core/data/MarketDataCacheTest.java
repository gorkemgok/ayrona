package com.ayronasystems.core.data;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.definition.SymbolPeriod;
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

    @Before
    public void setUp () throws Exception {
        List<Date> dates = new ArrayList<Date> (Arrays.asList (new Date[]{
                DateUtils.parseDate ("01.01.2016 01:00:00"),
                DateUtils.parseDate ("01.01.2016 01:05:00"),
                DateUtils.parseDate ("01.01.2016 01:10:00"),
                DateUtils.parseDate ("01.01.2016 01:15:00"),
                DateUtils.parseDate ("01.01.2016 01:20:00"),
                DateUtils.parseDate ("01.01.2016 01:25:00")
        }));
        double[] os = new double[]{0,1,2,3,4,5};
        double[] hs = new double[]{0,1,2,3,4,5};
        double[] ls = new double[]{0,1,2,3,4,5};
        double[] cs = new double[]{0,1,2,3,4,5};

        ohlc = new OHLC (Symbol.VOB30, Period.M5, dates, os, hs, ls, cs);
        marketDataCache = new MarketDataCache ();
        marketDataCache.cache (ohlc);

    }

    @Test
    public void get () throws Exception {
        SymbolPeriod symbolPeriod = new SymbolPeriod (Symbol.VOB30, Period.M5);

        MarketDataCacheResult r0 = marketDataCache.get (symbolPeriod,
                                                        new Interval (
                                                                DateUtils.parseDate ("01.01.2016 01:05:00"),
                                                                DateUtils.parseDate ("01.01.2016 01:25:00")
                                                        ));

        assertTrue (r0.isFoundData ());
        assertTrue (r0.getAbsentIntervals ().isEmpty ());
        assertEquals (4, r0.getFoundData ().size ());
        assertEquals (ohlc.getDates ().get (1), r0.getFoundData ().getDates ().get (0));
        assertEquals (ohlc.getDates ().get (2), r0.getFoundData ().getDates ().get (1));
        assertEquals (ohlc.getDates ().get (3), r0.getFoundData ().getDates ().get (2));
        assertEquals (ohlc.getDates ().get (4), r0.getFoundData ().getDates ().get (3));

        MarketDataCacheResult r1 = marketDataCache.get (symbolPeriod,
                                                        new Interval (
                                                                DateUtils.parseDate ("01.01.2016 01:00:00"),
                                                                DateUtils.parseDate ("01.01.2016 01:30:00")
                                                        ));

        assertTrue (r1.isFoundData ());
        assertTrue (r1.getAbsentIntervals ().isEmpty ());
        assertEquals (6, r1.getFoundData ().size ());
        assertEquals (ohlc.getDates ().get (0), r1.getFoundData ().getDates ().get (0));
        assertEquals (ohlc.getDates ().get (1), r1.getFoundData ().getDates ().get (1));
        assertEquals (ohlc.getDates ().get (2), r1.getFoundData ().getDates ().get (2));
        assertEquals (ohlc.getDates ().get (3), r1.getFoundData ().getDates ().get (3));
        assertEquals (ohlc.getDates ().get (4), r1.getFoundData ().getDates ().get (4));
        assertEquals (ohlc.getDates ().get (5), r1.getFoundData ().getDates ().get (5));

        MarketDataCacheResult r2 = marketDataCache.get (symbolPeriod,
                                                        new Interval (
                                                                DateUtils.parseDate ("01.01.2016 01:05:00"),
                                                                DateUtils.parseDate ("01.01.2016 01:30:00")
                                                        ));

        assertTrue (r2.isFoundData ());
        assertTrue (r2.getAbsentIntervals ().isEmpty ());
        assertEquals (5, r2.getFoundData ().size ());
        assertEquals (ohlc.getDates ().get (1), r2.getFoundData ().getDates ().get (0));
        assertEquals (ohlc.getDates ().get (2), r2.getFoundData ().getDates ().get (1));
        assertEquals (ohlc.getDates ().get (3), r2.getFoundData ().getDates ().get (2));
        assertEquals (ohlc.getDates ().get (4), r2.getFoundData ().getDates ().get (3));
        assertEquals (ohlc.getDates ().get (5), r2.getFoundData ().getDates ().get (4));

        MarketDataCacheResult r3 = marketDataCache.get (symbolPeriod,
                                                        new Interval (
                                                                DateUtils.parseDate ("01.01.2016 01:00:00"),
                                                                DateUtils.parseDate ("01.01.2016 01:25:00")
                                                        ));

        assertTrue (r3.isFoundData ());
        assertTrue (r3.getAbsentIntervals ().isEmpty ());
        assertEquals (5, r3.getFoundData ().size ());
        assertEquals (ohlc.getDates ().get (0), r3.getFoundData ().getDates ().get (0));
        assertEquals (ohlc.getDates ().get (1), r3.getFoundData ().getDates ().get (1));
        assertEquals (ohlc.getDates ().get (2), r3.getFoundData ().getDates ().get (2));
        assertEquals (ohlc.getDates ().get (3), r3.getFoundData ().getDates ().get (3));
        assertEquals (ohlc.getDates ().get (4), r3.getFoundData ().getDates ().get (4));

        MarketDataCacheResult r4 = marketDataCache.get (symbolPeriod,
                                                        new Interval (
                                                                DateUtils.parseDate ("01.01.2016 00:50:00"),
                                                                DateUtils.parseDate ("01.01.2016 01:15:00")
                                                        ));

        assertTrue (r4.isFoundData ());
        assertEquals (1, r4.getAbsentIntervals ().size ());
        assertEquals (DateUtils.parseDate ("01.01.2016 00:50:00"),
                      r4.getAbsentIntervals ().get (0).getBeginningDate ());
        assertEquals (DateUtils.parseDate ("01.01.2016 01:00:00"),
                      r4.getAbsentIntervals ().get (0).getEndingDate ());
        assertEquals (3, r4.getFoundData ().size ());
        assertEquals (ohlc.getDates ().get (0), r4.getFoundData ().getDates ().get (0));
        assertEquals (ohlc.getDates ().get (1), r4.getFoundData ().getDates ().get (1));
        assertEquals (ohlc.getDates ().get (2), r4.getFoundData ().getDates ().get (2));

        MarketDataCacheResult r5 = marketDataCache.get (symbolPeriod,
                                                        new Interval (
                                                                DateUtils.parseDate ("01.01.2016 01:15:00"),
                                                                DateUtils.parseDate ("01.01.2016 01:50:00")
                                                        ));

        assertTrue (r5.isFoundData ());
        assertEquals (1, r5.getAbsentIntervals ().size ());
        assertEquals (DateUtils.parseDate ("01.01.2016 01:30:00"),
                      r5.getAbsentIntervals ().get (0).getBeginningDate ());
        assertEquals (DateUtils.parseDate ("01.01.2016 01:50:00"),
                      r5.getAbsentIntervals ().get (0).getEndingDate ());
        assertEquals (3, r5.getFoundData ().size ());
        assertEquals (ohlc.getDates ().get (3), r5.getFoundData ().getDates ().get (0));
        assertEquals (ohlc.getDates ().get (4), r5.getFoundData ().getDates ().get (1));
        assertEquals (ohlc.getDates ().get (5), r5.getFoundData ().getDates ().get (2));

        MarketDataCacheResult r6 = marketDataCache.get (symbolPeriod,
                                                        new Interval (
                                                                DateUtils.parseDate ("01.01.2016 00:50:00"),
                                                                DateUtils.parseDate ("01.01.2016 01:30:00")
                                                        ));

        assertTrue (r6.isFoundData ());
        assertEquals (2, r6.getAbsentIntervals ().size ());
        assertEquals (DateUtils.parseDate ("01.01.2016 00:50:00"),
                      r6.getAbsentIntervals ().get (0).getBeginningDate ());
        assertEquals (DateUtils.parseDate ("01.01.2016 01:00:00"),
                      r6.getAbsentIntervals ().get (0).getEndingDate ());
        assertEquals (6, r6.getFoundData ().size ());
        assertEquals (DateUtils.parseDate ("01.01.2016 01:30:00"),
                      r6.getAbsentIntervals ().get (1).getBeginningDate ());
        assertEquals (DateUtils.parseDate ("01.01.2016 01:30:00"),
                      r6.getAbsentIntervals ().get (1).getEndingDate ());
        assertEquals (6, r6.getFoundData ().size ());
        assertEquals (ohlc.getDates ().get (0), r6.getFoundData ().getDates ().get (0));
        assertEquals (ohlc.getDates ().get (1), r6.getFoundData ().getDates ().get (1));
        assertEquals (ohlc.getDates ().get (2), r6.getFoundData ().getDates ().get (2));
        assertEquals (ohlc.getDates ().get (3), r6.getFoundData ().getDates ().get (3));
        assertEquals (ohlc.getDates ().get (4), r6.getFoundData ().getDates ().get (4));
        assertEquals (ohlc.getDates ().get (5), r6.getFoundData ().getDates ().get (5));

    }

}