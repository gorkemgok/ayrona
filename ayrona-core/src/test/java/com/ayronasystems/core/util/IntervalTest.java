package com.ayronasystems.core.util;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by gorkemgok on 12/06/16.
 */
public class IntervalTest {

    @Test
    public void contains () throws Exception {
        Interval interval = new Interval (
                DateUtils.parseDate ("01.01.2016 09:00:00"), DateUtils.parseDate ("01.01.2016 10:00:00")
        );

        boolean a0 = interval.contains (DateUtils.parseDate ("01.01.2016 08:59:00"));
        boolean a1 = interval.contains (DateUtils.parseDate ("01.01.2016 09:00:00"));
        boolean a2 = interval.contains (DateUtils.parseDate ("01.01.2016 09:59:00"));
        boolean a3 = interval.contains (DateUtils.parseDate ("01.01.2016 10:00:00"));

        assertFalse (a0);
        assertTrue(a1);
        assertTrue (a2);
        assertFalse (a3);
    }

    @Test
    public void contains1 () throws Exception {
        Interval interval = new Interval (
                DateUtils.parseDate ("01.01.2016 09:00:00"), DateUtils.parseDate ("01.01.2016 10:00:00")
        );
        boolean a0 = interval.contains (DateUtils.parseDate ("01.01.2016 08:59:00"), DateUtils.parseDate ("01.01.2016 09:59:00"));
        boolean a1 = interval.contains (DateUtils.parseDate ("01.01.2016 09:00:00"), DateUtils.parseDate ("01.01.2016 09:59:00"));
        boolean a2 = interval.contains (DateUtils.parseDate ("01.01.2016 09:00:00"), DateUtils.parseDate ("01.01.2016 10:00:00"));
        boolean a3 = interval.contains (DateUtils.parseDate ("01.01.2016 09:00:00"), DateUtils.parseDate ("01.01.2016 10:01:00"));

        assertFalse (a0);
        assertTrue(a1);
        assertTrue (a2);
        assertFalse (a3);
    }

    @Test
    public void contains2 () throws Exception {
        Interval interval = new Interval (
                DateUtils.parseDate ("01.01.2016 09:00:00"), DateUtils.parseDate ("01.01.2016 10:00:00")
        );
        boolean a0 = interval.contains (new Interval (DateUtils.parseDate ("01.01.2016 08:59:00"), DateUtils.parseDate ("01.01.2016 09:59:00")));
        assertFalse (a0);

    }

    @Test
    public void overlapsAtLeft(){
        Interval interval = new Interval (
                DateUtils.parseDate ("01.01.2016 09:00:00"), DateUtils.parseDate ("01.01.2016 10:00:00")
        );

        boolean false0 = interval.overlapsAtLeft (DateUtils.parseDate ("01.01.2016 08:50:00"),
                                              DateUtils.parseDate ("01.01.2016 08:59:00")
        );
        boolean false1 = interval.overlapsAtLeft (DateUtils.parseDate ("01.01.2016 08:50:00"),
                                              DateUtils.parseDate ("01.01.2016 09:00:00")
        );
        boolean false2 = interval.overlapsAtLeft (DateUtils.parseDate ("01.01.2016 09:00:00"),
                                              DateUtils.parseDate ("01.01.2016 09:10:00")
        );
        boolean false3 = interval.overlapsAtLeft (DateUtils.parseDate ("01.01.2016 09:10:00"),
                                              DateUtils.parseDate ("01.01.2016 09:20:00")
        );
        boolean false4 = interval.overlapsAtLeft (DateUtils.parseDate ("01.01.2016 08:59:00"),
                                                  DateUtils.parseDate ("01.01.2016 10:01:00")
        );
        boolean false5 = interval.overlapsAtLeft (DateUtils.parseDate ("01.01.2016 08:59:00"),
                                                  DateUtils.parseDate ("01.01.2016 09:01:00")
        );
        boolean false6 = interval.overlapsAtLeft (DateUtils.parseDate ("01.01.2016 08:59:00"),
                                                 DateUtils.parseDate ("01.01.2016 10:00:00")
        );
        boolean true0 = interval.overlapsAtLeft (DateUtils.parseDate ("01.01.2016 09:10:00"),
                                                 DateUtils.parseDate ("01.01.2016 10:00:00")
        );
        boolean true1 = interval.overlapsAtLeft (DateUtils.parseDate ("01.01.2016 09:10:00"),
                                                  DateUtils.parseDate ("01.01.2016 10:10:00")
        );

        assertFalse (false0);
        assertFalse (false1);
        assertFalse (false2);
        assertFalse (false3);
        assertFalse (false4);
        assertFalse (false5);
        assertFalse (false6);
        assertTrue (true0);
        assertTrue (true1);
    }

    @Test
    public void overlapsAtRight(){
        Interval interval = new Interval (
                DateUtils.parseDate ("01.01.2016 09:00:00"), DateUtils.parseDate ("01.01.2016 10:00:00")
        );

        boolean false0 = interval.overlapsAtRight (DateUtils.parseDate ("01.01.2016 08:50:00"),
                                                  DateUtils.parseDate ("01.01.2016 08:59:00")
        );
        boolean false1 = interval.overlapsAtRight (DateUtils.parseDate ("01.01.2016 08:50:00"),
                                                  DateUtils.parseDate ("01.01.2016 09:00:00")
        );
        boolean false2 = interval.overlapsAtRight (DateUtils.parseDate ("01.01.2016 09:10:00"),
                                                  DateUtils.parseDate ("01.01.2016 09:20:00")
        );
        boolean false3 = interval.overlapsAtRight (DateUtils.parseDate ("01.01.2016 08:59:00"),
                                                  DateUtils.parseDate ("01.01.2016 10:01:00")
        );
        boolean false4 = interval.overlapsAtRight (DateUtils.parseDate ("01.01.2016 09:10:00"),
                                                 DateUtils.parseDate ("01.01.2016 10:00:00")
        );
        boolean false5 = interval.overlapsAtRight (DateUtils.parseDate ("01.01.2016 09:10:00"),
                                                 DateUtils.parseDate ("01.01.2016 10:10:00")
        );
        boolean false6 = interval.overlapsAtRight (DateUtils.parseDate ("01.01.2016 08:59:00"),
                                                   DateUtils.parseDate ("01.01.2016 10:00:00")
        );
        boolean true0 = interval.overlapsAtRight (DateUtils.parseDate ("01.01.2016 08:59:00"),
                                                  DateUtils.parseDate ("01.01.2016 09:01:00")
        );
        boolean true1 = interval.overlapsAtRight (DateUtils.parseDate ("01.01.2016 09:00:00"),
                                                   DateUtils.parseDate ("01.01.2016 09:10:00")
        );

        assertFalse (false0);
        assertFalse (false1);
        assertFalse (false2);
        assertFalse (false3);
        assertFalse (false4);
        assertFalse (false5);
        assertFalse (false6);
        assertTrue (true0);
        assertTrue (true1);
    }

    @Test
    public void extractNot(){
        Interval interval1 = new Interval (DateUtils.parseDate ("01.01.2016 09:00:00"), DateUtils.parseDate ("01.01.2016 12:00:00"));

        Interval interval2 = new Interval (DateUtils.parseDate ("01.01.2016 08:00:00"), DateUtils.parseDate ("01.01.2016 12:00:00"));
        Interval interval3 = new Interval (DateUtils.parseDate ("01.01.2016 12:00:00"), DateUtils.parseDate ("01.01.2016 13:00:00"));
        Interval interval4 = new Interval (DateUtils.parseDate ("01.01.2016 09:00:00"), DateUtils.parseDate ("01.01.2016 13:00:00"));
        Interval interval5 = new Interval (DateUtils.parseDate ("01.01.2016 07:00:00"), DateUtils.parseDate ("01.01.2016 09:00:00"));
        Interval interval6 = new Interval (DateUtils.parseDate ("01.01.2016 08:00:00"), DateUtils.parseDate ("01.01.2016 10:00:00"));

        List<Interval> actualAbsentList = interval1.extractNot (interval2);
        assertEquals (1, actualAbsentList.size ());
        Interval actualAbsent = actualAbsentList.get (0);
        assertEquals (DateUtils.parseDate ("01.01.2016 08:00:00"), actualAbsent.getBeginningDate ());
        assertEquals (DateUtils.parseDate ("01.01.2016 09:00:00"), actualAbsent.getEndingDate ());

        actualAbsentList = interval1.extractNot (interval3);
        assertEquals (1, actualAbsentList.size ());
        actualAbsent = actualAbsentList.get (0);
        assertEquals (DateUtils.parseDate ("01.01.2016 12:00:00"), actualAbsent.getBeginningDate ());
        assertEquals (DateUtils.parseDate ("01.01.2016 13:00:00"), actualAbsent.getEndingDate ());

        actualAbsentList = interval1.extractNot (interval4);
        assertEquals (1, actualAbsentList.size ());
        actualAbsent = actualAbsentList.get (0);
        assertEquals (DateUtils.parseDate ("01.01.2016 12:00:00"), actualAbsent.getBeginningDate ());
        assertEquals (DateUtils.parseDate ("01.01.2016 13:00:00"), actualAbsent.getEndingDate ());

        actualAbsentList = interval1.extractNot (interval5);
        assertEquals (1, actualAbsentList.size ());
        actualAbsent = actualAbsentList.get (0);
        assertEquals (DateUtils.parseDate ("01.01.2016 07:00:00"), actualAbsent.getBeginningDate ());
        assertEquals (DateUtils.parseDate ("01.01.2016 09:00:00"), actualAbsent.getEndingDate ());

        actualAbsentList = interval1.extractNot (interval6);
        assertEquals (1, actualAbsentList.size ());
        actualAbsent = actualAbsentList.get (0);
        assertEquals (DateUtils.parseDate ("01.01.2016 08:00:00"), actualAbsent.getBeginningDate ());
        assertEquals (DateUtils.parseDate ("01.01.2016 09:00:00"), actualAbsent.getEndingDate ());
    }

}