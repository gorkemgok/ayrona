package com.ayronasystems.core.data;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.definition.Symbols;
import com.ayronasystems.core.exception.CorruptedMarketDataException;
import com.ayronasystems.core.exception.MarketDataConversionException;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.ayronasystems.core.util.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gorkemgok on 31/05/16.
 */
public class OHLCTest {

    public static final Bar[] BARS_M1 = {
            new Bar(DateUtils.parseDate("01.01.2016 00:59:00"), 123676, 123683, 123667, 123682, 0),//0
            new Bar(DateUtils.parseDate("01.01.2016 01:00:00"), 113676, 113683, 113667, 113682, 0),//0
            new Bar(DateUtils.parseDate("01.01.2016 01:01:00"), 113681, 113771, 11367, 113764, 0),//1
            new Bar(DateUtils.parseDate("01.01.2016 01:02:00"), 113742, 113781, 113731, 113773, 0),//2
            new Bar(DateUtils.parseDate("01.01.2016 01:03:00"), 113775, 120000, 113738, 113749, 0),//3
            new Bar(DateUtils.parseDate("01.01.2016 01:04:00"), 113749, 113753, 113740, 113750, 0),//4
            new Bar(DateUtils.parseDate("01.01.2016 01:05:00"), 113749, 113768, 113749, 113763, 0),//5
            new Bar(DateUtils.parseDate("01.01.2016 01:06:00"), 113760, 123763, 113758, 113762, 0),//6
            new Bar(DateUtils.parseDate("01.01.2016 01:07:00"), 113763, 113792, 113761, 113791, 0),//7
            new Bar(DateUtils.parseDate("01.01.2016 01:08:00"), 113791, 113791, 110770, 113780, 0),//8
            new Bar(DateUtils.parseDate("01.01.2016 01:09:00"), 113779, 113782, 113777, 113779, 0),//9
            new Bar(DateUtils.parseDate("01.01.2016 01:10:00"), 113783, 113786, 113766, 113767, 0),//10
            new Bar(DateUtils.parseDate("01.01.2016 01:11:00"), 113767, 113772, 113764, 113772, 0),//11
            new Bar(DateUtils.parseDate("01.01.2016 01:12:00"), 113769, 113769, 113757, 113759, 0),//12
            new Bar(DateUtils.parseDate("01.01.2016 01:13:00"), 113757, 113771, 113757, 113771, 0),//13
            new Bar(DateUtils.parseDate("01.01.2016 01:14:00"), 113769, 133773, 103768, 113770, 0),//14
            new Bar(DateUtils.parseDate("01.01.2016 01:15:00"), 113771, 133791, 103770, 113779, 0),//15
            new Bar(DateUtils.parseDate("01.01.2016 01:16:00"), 113780, 113811, 113778, 113811, 0),//16
            new Bar(DateUtils.parseDate("01.01.2016 01:17:00"), 113810, 113868, 113810, 113848, 0),//17
            new Bar(DateUtils.parseDate("01.01.2016 01:18:00"), 113847, 113880, 113845, 113870, 0),//18
            new Bar(DateUtils.parseDate("01.01.2016 01:19:00"), 113871, 113882, 113862, 113867, 0),//19
            new Bar(DateUtils.parseDate("01.01.2016 01:20:00"), 113867, 113897, 113863, 113880, 0),//20
            new Bar(DateUtils.parseDate("01.01.2016 01:21:00"), 113879, 113897, 113876, 113895, 0) //21
    };

    private OHLC ohlc1;

    private OHLC ohlc2;

    private OHLC ohlc3;

    @Before
    public void setUp () throws Exception {
        List<Date> dates1 = new ArrayList<Date> (Arrays.asList (new Date[]{
                DateUtils.parseDate ("01.01.2016 01:00:00"),
                DateUtils.parseDate ("01.01.2016 01:05:00")
        }));
        List<Date> dates2 = new ArrayList<Date> (Arrays.asList (new Date[]{
                DateUtils.parseDate ("01.01.2016 01:10:00"),
                DateUtils.parseDate ("01.01.2016 01:15:00"),
                DateUtils.parseDate ("01.01.2016 01:20:00"),
                DateUtils.parseDate ("01.01.2016 01:25:00")
        }));
        List<Date> dates3 = new ArrayList<Date> (Arrays.asList (new Date[]{
                DateUtils.parseDate ("01.01.2016 01:30:00"),
                DateUtils.parseDate ("01.01.2016 01:35:00"),
                DateUtils.parseDate ("01.01.2016 01:40:00"),
                DateUtils.parseDate ("01.01.2016 01:45:00")
        }));
        double[] os1 = new double[]{0.1,1.1};
        double[] hs1 = new double[]{1.1,2.1};
        double[] ls1 = new double[]{2.1,3.1};
        double[] cs1 = new double[]{3.1,4.1};

        double[] os2 = new double[]{0,1,2,3};
        double[] hs2 = new double[]{0,1,2,3};
        double[] ls2 = new double[]{0,1,2,3};
        double[] cs2 = new double[]{0,1,2,3};

        ohlc1 = new OHLC (Symbols.of("TEST"), Period.M5, dates1, os1, hs1, ls1, cs1);
        ohlc2 = new OHLC (Symbols.of("TEST"), Period.M5, dates2, os2, hs2, ls2, cs2);
        ohlc3 = new OHLC (Symbols.of("TEST"), Period.M5, dates3, os2, hs2, ls2, cs2);

    }

    @Test
    public void subData () throws Exception {
        List<Date> dates = new ArrayList<Date> (Arrays.asList (new Date[]{
                new Date(),new Date(),new Date(),new Date(),new Date(), new Date()
        }));
        double[] os = new double[]{0,1,2,3,4,5};
        double[] hs = new double[]{0,1,2,3,4,5};
        double[] ls = new double[]{0,1,2,3,4,5};
        double[] cs = new double[]{0,1,2,3,4,5};

        MarketData md = new OHLC (Symbols.of("TEST"), Period.M5, dates, os, hs, ls, cs);
        MarketData subMd = md.subData (0, 3);

        assertEquals (4, subMd.size ());
        assertEquals (0, subMd.getPrice (PriceColumn.OPEN, 0), 0);
        assertEquals (1, subMd.getPrice (PriceColumn.OPEN, 1), 0);
        assertEquals (2, subMd.getPrice (PriceColumn.OPEN, 2), 0);
        assertEquals (3, subMd.getPrice (PriceColumn.OPEN, 3), 0);

    }

    @Test
    public void merge () throws Exception {
        MarketData md1 = ohlc2.merge(ohlc1);
        assertEquals (6, md1.size ());
        assertEquals (DateUtils.parseDate ("01.01.2016 01:00:00"), md1.getBeginningDate ());
        assertEquals (DateUtils.parseDate ("01.01.2016 01:30:00"), md1.getEndingDate ());
        assertEquals (0.1, md1.getPrice (PriceColumn.OPEN, 0), 0);
        assertEquals (3, md1.getPrice (PriceColumn.OPEN, md1.size () - 1), 0);

        MarketData md2 = ohlc2.merge(ohlc3);
        assertEquals (8, md2.size ());
        assertEquals (DateUtils.parseDate ("01.01.2016 01:10:00"), md2.getBeginningDate ());
        assertEquals (DateUtils.parseDate ("01.01.2016 01:50:00"), md2.getEndingDate ());

        assertEquals (0, md2.getPrice (PriceColumn.OPEN, 0), 0);
        assertEquals (3, md2.getPrice (PriceColumn.OPEN, md2.size () - 1), 0);

    }

    @Test
    public void convert() throws CorruptedMarketDataException, MarketDataConversionException {
        MarketData m1Md = new OHLC (Symbols.of("TEST"), Period.M1, Arrays.asList (BARS_M1));
        MarketData m5Md = m1Md.convert (Period.M5);
        assertEquals (5, m5Md.size ());

        assertEquals (DateUtils.parseDate("01.01.2016 00:55:00"),
                      m5Md.getDate (0));
        assertEquals (123676, m5Md.getPrice (PriceColumn.OPEN, 0), 0);
        assertEquals (123683, m5Md.getPrice (PriceColumn.HIGH, 0), 0);
        assertEquals (123667, m5Md.getPrice (PriceColumn.LOW, 0), 0);
        assertEquals (123682, m5Md.getPrice (PriceColumn.CLOSE, 0), 0);

        assertEquals (DateUtils.parseDate("01.01.2016 01:00:00"),
                      m5Md.getDate (1));
        assertEquals (113676, m5Md.getPrice (PriceColumn.OPEN, 1), 0);
        assertEquals (120000, m5Md.getPrice (PriceColumn.HIGH, 1), 0);
        assertEquals (11367, m5Md.getPrice (PriceColumn.LOW, 1), 0);
        assertEquals (113750, m5Md.getPrice (PriceColumn.CLOSE, 1), 0);

        assertEquals (DateUtils.parseDate("01.01.2016 01:05:00"),
                      m5Md.getDate (2));
        assertEquals (113749, m5Md.getPrice (PriceColumn.OPEN, 2), 0);
        assertEquals (123763, m5Md.getPrice (PriceColumn.HIGH, 2), 0);
        assertEquals (110770, m5Md.getPrice (PriceColumn.LOW, 2), 0);
        assertEquals (113779, m5Md.getPrice (PriceColumn.CLOSE, 2), 0);

        assertEquals (DateUtils.parseDate("01.01.2016 01:10:00"),
                      m5Md.getDate (3));
        assertEquals (113783, m5Md.getPrice (PriceColumn.OPEN, 3), 0);
        assertEquals (133773, m5Md.getPrice (PriceColumn.HIGH, 3), 0);
        assertEquals (103768, m5Md.getPrice (PriceColumn.LOW, 3), 0);
        assertEquals (113770, m5Md.getPrice (PriceColumn.CLOSE, 3), 0);

        assertEquals (DateUtils.parseDate("01.01.2016 01:15:00"),
                      m5Md.getDate (4));
        assertEquals (113771, m5Md.getPrice (PriceColumn.OPEN, 4), 0);
        assertEquals (133791, m5Md.getPrice (PriceColumn.HIGH, 4), 0);
        assertEquals (103770, m5Md.getPrice (PriceColumn.LOW, 4), 0);
        assertEquals (113867, m5Md.getPrice (PriceColumn.CLOSE, 4), 0);
    }
}