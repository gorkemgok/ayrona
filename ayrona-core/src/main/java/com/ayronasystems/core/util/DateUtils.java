package com.ayronasystems.core.util;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gorkemgok on 11/05/16.
 */
public class DateUtils {

    private static Logger log = LoggerFactory.getLogger (DateUtils.class);

    public static final SimpleDateFormat SDF = new SimpleDateFormat ("dd.MM.yyyy HH:mm:ss");

    public static String formatDate(Date date){
        return SDF.format (date);
    }

    public static Date parseDate(String date){
        try {
            return SDF.parse (date);
        } catch ( ParseException e ) {
            log.error ("Parse date error", e);
            return new Date(0);
        }
    }

    public static boolean isInInterval(Date start, Date end, Date dateToBeControlled){
        if ( start != null && end != null && dateToBeControlled != null) {
            Interval interval = new Interval (start.getTime (), end.getTime () + 1);
            return interval.contains (dateToBeControlled.getTime ());
        }
        return false;
    }

    public static Date convertFromISO(String date){
        return new DateTime (date).toDate ();
    }

    public static String toISO(Date date){
        DateTime dateTime = new DateTime (date);
        return dateTime.toString ();
    }
}
