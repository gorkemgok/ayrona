package com.ayronasystems.core;

import com.ayronasystems.core.util.DateUtils;
import com.ayronasystems.core.util.Interval;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by gorkemgok on 09/08/16.
 */
public class MarketCalendarTest {

    private static final String TEST_EXP_1 = "01.01.2015-01.01.2016 09:10-17:45";


    @Test
    public void isMarketOpen () throws Exception {
        MarketCalendar marketCalendar = new MarketCalendar ();
        marketCalendar.include (TEST_EXP_1, 1, 7);

        long start = System.currentTimeMillis ();
        assertEquals (false, marketCalendar.isMarketOpen (DateUtils.parseDate ("01.01.2015 09:09:59")));
        assertEquals (true, marketCalendar.isMarketOpen (DateUtils.parseDate ("01.01.2015 09:10:00")));
        assertEquals (true, marketCalendar.isMarketOpen (DateUtils.parseDate ("01.01.2015 17:40:00")));
        assertEquals (false, marketCalendar.isMarketOpen (DateUtils.parseDate ("01.01.2015 17:45:00")));
        assertEquals (false, marketCalendar.isMarketOpen (DateUtils.parseDate ("07.02.2015 10:45:00")));
        long end = System.currentTimeMillis ();
        System.out.println ("Controls market days in "+(end-start)+" ms");
    }

    @Test
    public void importExpression () throws Exception {
        MarketCalendar marketCalendar = new MarketCalendar ();
        marketCalendar.include (TEST_EXP_1, 1, 7);
        List<Interval> businessDaysInterval = marketCalendar.getBusinessDayIntervals ();
        for (Interval businessDay : businessDaysInterval){
            System.out.println (businessDay);
        }
    }

}