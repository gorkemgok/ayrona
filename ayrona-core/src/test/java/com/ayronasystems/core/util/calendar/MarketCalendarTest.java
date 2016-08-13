package com.ayronasystems.core.util.calendar;

import com.ayronasystems.core.util.DateUtils;
import com.ayronasystems.core.util.Interval;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by gorkemgok on 09/08/16.
 */
public class MarketCalendarTest {

    private static final String TEST_INCLUDE = "01.01.2015-01.01.2016 09:10-17:45";
    private static final String TEST_EXCLUDE = "01.01.2015-01.01.2016 13:00-14:00";
    private static final String TEST_HOLIDAY = "05.01.2015-07.01.2015 00:00-23:59";

    @Test
    public void isMarketOpen () throws Exception {
        MarketCalendar marketCalendar = new MarketCalendar ();
        marketCalendar.include (TEST_INCLUDE, 1, 7);
        marketCalendar.exclude (TEST_EXCLUDE, 1, 7);

        long start = System.currentTimeMillis ();
        assertEquals (false, marketCalendar.isMarketOpen (DateUtils.parseDate ("01.01.2015 09:09:59")));
        assertEquals (true, marketCalendar.isMarketOpen (DateUtils.parseDate ("01.01.2015 09:10:00")));
        assertEquals (true, marketCalendar.isMarketOpen (DateUtils.parseDate ("01.01.2015 17:40:00")));
        assertEquals (false, marketCalendar.isMarketOpen (DateUtils.parseDate ("01.01.2015 17:45:00")));
        assertEquals (false, marketCalendar.isMarketOpen (DateUtils.parseDate ("07.02.2015 10:45:00")));

        assertEquals (true, marketCalendar.isMarketOpen (DateUtils.parseDate ("01.01.2015 12:59:00")));
        assertEquals (false, marketCalendar.isMarketOpen (DateUtils.parseDate ("01.01.2015 13:00:00")));
        assertEquals (true, marketCalendar.isMarketOpen (DateUtils.parseDate ("01.01.2015 14:00:00")));

        assertEquals (true, marketCalendar.isMarketOpen (DateUtils.parseDate ("05.01.2015 09:10:00")));
        assertEquals (true, marketCalendar.isMarketOpen (DateUtils.parseDate ("06.01.2015 17:40:00")));

        marketCalendar.exclude (TEST_HOLIDAY);

        assertEquals (false, marketCalendar.isMarketOpen (DateUtils.parseDate ("05.01.2015 09:10:00")));
        assertEquals (false, marketCalendar.isMarketOpen (DateUtils.parseDate ("06.01.2015 17:40:00")));

        long end = System.currentTimeMillis ();
        System.out.println ("Controls market days in "+(end-start)+" ms");
    }

    @Test
    public void importExpression () throws Exception {
        MarketCalendar marketCalendar = new MarketCalendar ();
        marketCalendar.include (TEST_INCLUDE, 1, 7);
        for ( DayIntervals dayIntervals : marketCalendar.getDayIntervalsList () ) {
            List<Interval> businessDaysInterval = dayIntervals.getDayIntervals ();
            for ( Interval businessDay : businessDaysInterval ) {
                System.out.println ((dayIntervals.isOff ()?"OFF":"BD")+" - "+businessDay);
            }
        }
    }

}