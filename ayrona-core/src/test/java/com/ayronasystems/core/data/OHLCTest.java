package com.ayronasystems.core.data;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.definition.Symbol;
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

        ohlc1 = new OHLC (Symbol.VOB30, Period.M5, dates1, os1, hs1, ls1, cs1);
        ohlc2 = new OHLC (Symbol.VOB30, Period.M5, dates2, os2, hs2, ls2, cs2);
        ohlc3 = new OHLC (Symbol.VOB30, Period.M5, dates3, os2, hs2, ls2, cs2);

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

        MarketData md = new OHLC (Symbol.VOB30, Period.M5, dates, os, hs, ls, cs);
        MarketData subMd = md.subData (0, 3);

        assertEquals (4, subMd.size ());
        assertEquals (0, subMd.getPrice (PriceColumn.OPEN, 0), 0);
        assertEquals (1, subMd.getPrice (PriceColumn.OPEN, 1), 0);
        assertEquals (2, subMd.getPrice (PriceColumn.OPEN, 2), 0);
        assertEquals (3, subMd.getPrice (PriceColumn.OPEN, 3), 0);

    }

    @Test
    public void append () throws Exception {
        MarketData md1 = ohlc2.append (ohlc1);
        assertEquals (6, md1.size ());
        assertEquals (DateUtils.parseDate ("01.01.2016 01:00:00"), md1.getBeginningDate ());
        assertEquals (DateUtils.parseDate ("01.01.2016 01:30:00"), md1.getEndingDate ());
        assertEquals (0.1, md1.getPrice (PriceColumn.OPEN, 0), 0);
        assertEquals (3, md1.getPrice (PriceColumn.OPEN, md1.size () - 1), 0);

        MarketData md2 = ohlc2.append (ohlc3);
        assertEquals (8, md2.size ());
        assertEquals (DateUtils.parseDate ("01.01.2016 01:10:00"), md2.getBeginningDate ());
        assertEquals (DateUtils.parseDate ("01.01.2016 01:50:00"), md2.getEndingDate ());

        assertEquals (0, md2.getPrice (PriceColumn.OPEN, 0), 0);
        assertEquals (3, md2.getPrice (PriceColumn.OPEN, md2.size () - 1), 0);

    }
}