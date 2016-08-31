package com.ayronasystems.core.util.date;

import com.ayronasystems.core.util.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * Created by gorkemgok on 29/08/16.
 */
public class DatePeriodComparatorTest {

    private DatePeriodComparator dayComp;
    private DatePeriodComparator weekComp;
    private DatePeriodComparator monthComp;
    private DatePeriodComparator yearComp;

    @Before
    public void setup(){
        dayComp = new DayComparator ();
        weekComp = new WeekComparator ();
        monthComp = new MonthComparator ();
        yearComp = new YearComparator ();
    }

    @Test
    public void isSameDay(){
        Date date1 = DateUtils.parseDate ("01.01.2010 00:00:00");
        Date date2 = DateUtils.parseDate ("01.01.2010 01:00:00");
        Date date3 = DateUtils.parseDate ("02.01.2010 00:00:00");
        Date date4 = DateUtils.parseDate ("01.01.2011 01:00:00");

        Assert.assertTrue (dayComp.isSamePeriod (date1, date2));
        Assert.assertFalse (dayComp.isSamePeriod (date1, date3));
        Assert.assertFalse (dayComp.isSamePeriod (date1, date4));

    }

    @Test
    public void isSameWeek(){
        Date date1 = DateUtils.parseDate ("01.01.2016 00:00:00");
        Date date2 = DateUtils.parseDate ("02.01.2016 01:00:00");
        Date date3 = DateUtils.parseDate ("04.01.2016 00:00:00");
        Date date4 = DateUtils.parseDate ("31.12.2015 01:00:00");

        Assert.assertTrue (weekComp.isSamePeriod (date1, date2));
        Assert.assertFalse (weekComp.isSamePeriod (date1, date3));
        Assert.assertFalse (weekComp.isSamePeriod (date1, date4));

    }

    @Test
    public void isSameMonth(){
        Date date1 = DateUtils.parseDate ("01.01.2016 00:00:00");
        Date date2 = DateUtils.parseDate ("31.01.2016 01:00:00");
        Date date3 = DateUtils.parseDate ("01.02.2016 00:00:00");
        Date date4 = DateUtils.parseDate ("31.12.2015 01:00:00");

        Assert.assertTrue (monthComp.isSamePeriod (date1, date2));
        Assert.assertFalse (monthComp.isSamePeriod (date1, date3));
        Assert.assertFalse (monthComp.isSamePeriod (date1, date4));
    }

    @Test
    public void isSameYear(){
        Date date1 = DateUtils.parseDate ("01.01.2016 00:00:00");
        Date date2 = DateUtils.parseDate ("01.02.2016 00:00:00");
        Date date3 = DateUtils.parseDate ("02.01.2015 01:00:00");

        Assert.assertTrue (yearComp.isSamePeriod (date1, date2));
        Assert.assertFalse (yearComp.isSamePeriod (date1, date3));

    }

}