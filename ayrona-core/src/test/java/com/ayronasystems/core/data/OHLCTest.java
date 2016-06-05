package com.ayronasystems.core.data;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.definition.Symbol;
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

        assertEquals (4, subMd.getDataCount ());
        assertEquals (0, subMd.getData (PriceColumn.OPEN, 0), 0);
        assertEquals (1, subMd.getData (PriceColumn.OPEN, 1), 0);
        assertEquals (2, subMd.getData (PriceColumn.OPEN, 2), 0);
        assertEquals (3, subMd.getData (PriceColumn.OPEN, 3), 0);

    }

}