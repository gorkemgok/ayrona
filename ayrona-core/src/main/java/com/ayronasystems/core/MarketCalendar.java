package com.ayronasystems.core;

import com.ayronasystems.core.util.Interval;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 09/08/16.
 */
public class MarketCalendar {

    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

    private static final SimpleDateFormat SDF = new SimpleDateFormat ("dd.MM.yyyy");

    private List<Interval> businessDayIntervals = new ArrayList<Interval> ();

    private Calendar cal = Calendar.getInstance ();

    public boolean isMarketOpen(Date date){
        for ( Interval interval : businessDayIntervals ){
            return interval.contains (date);
        }
        return false;
    }

    public void include(String expression, int... excludeDays) throws ParseException {
        String[] dateTime = expression.split (" ");
        if (dateTime.length == 2) {
            String[] startEndDate = dateTime[0].split ("-");
            String[] startEndTime = dateTime[1].split ("-");
            if (startEndDate.length == 1){
                Date date = SDF.parse (startEndDate[0]);
                addInterval ( date, dateTime, startEndTime, excludeDays);
            }else{
                Date startDate = SDF.parse (startEndDate[0]);
                Date endDate = SDF.parse (startEndDate[1]);
                for ( long i = startDate.getTime (); i < endDate.getTime (); i +=  DAY_IN_MILLIS) {
                    Date date = new Date(i);
                    addInterval (date, dateTime, startEndTime, excludeDays);
                }
            }
        }else{
            throw new ParseException (expression, expression.length ());
        }
    }

    private void addInterval (Date date, String[] dateTime, String[] startEndTime, int... excludeDays) throws ParseException {
        Date intervalDateStart;
        Date intervalDateEnd;
        cal.setTime (date);
        boolean exclude = false;
        int day = cal.get (Calendar.DAY_OF_WEEK);
        for(int excludeDay : excludeDays){
            if (day == excludeDay){
                exclude = true;
                break;
            }
        }
        if (!exclude) {
            if ( startEndTime.length == 2 ) {
                try {
                    String[] startHourMin = startEndTime[0].split (":");
                    cal.set (Calendar.HOUR_OF_DAY, Integer.valueOf (startHourMin[0]));
                    cal.set (Calendar.MINUTE, Integer.valueOf (startHourMin[1]));
                    intervalDateStart = cal.getTime ();
                    String[] endHourMin = startEndTime[1].split (":");
                    cal.set (Calendar.HOUR_OF_DAY, Integer.valueOf (endHourMin[0]));
                    cal.set (Calendar.MINUTE, Integer.valueOf (endHourMin[1]));
                    intervalDateEnd = cal.getTime ();
                    businessDayIntervals.add (new Interval (intervalDateStart, intervalDateEnd));
                } catch ( NumberFormatException e ) {
                    throw new ParseException (startEndTime[0], startEndTime[0].length ());
                }
            } else {
                throw new ParseException (dateTime[1], dateTime[1].length ());
            }
        }
    }

    public List<Interval> getBusinessDayIntervals () {
        return new ArrayList<Interval> (businessDayIntervals);
    }
}
