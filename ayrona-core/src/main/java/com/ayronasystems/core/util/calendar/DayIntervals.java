package com.ayronasystems.core.util.calendar;

import com.ayronasystems.core.util.Interval;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 12/08/16.
 */
public class DayIntervals {

    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

    private static final SimpleDateFormat SDF = new SimpleDateFormat ("dd.MM.yyyy");

    private List<Interval> dayIntervals = new ArrayList<Interval> ();

    private Calendar cal = Calendar.getInstance ();

    private boolean isOff;

    public static DayIntervals createOffDays(String expression, int... excludeDays) throws ParseException {
        return new DayIntervals (expression, true, excludeDays);
    }

    public static DayIntervals createBusinessDays(String expression, int... excludeDays) throws ParseException {
        return new DayIntervals (expression, false, excludeDays);
    }

    private DayIntervals(String expression, boolean isOff, int... excludeDays) throws ParseException {
        this.isOff = isOff;
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

    public boolean isOff () {
        return isOff;
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
                    String[] startHourMinSec = startEndTime[0].split (":");
                    cal.set (Calendar.HOUR_OF_DAY, Integer.valueOf (startHourMinSec[0]));
                    cal.set (Calendar.MINUTE, Integer.valueOf (startHourMinSec[1]));
                    if (startHourMinSec.length < 3){
                        cal.set (Calendar.SECOND, 0);
                    }else{
                        cal.set (Calendar.SECOND, Integer.valueOf (startHourMinSec[2]));
                    }
                    intervalDateStart = cal.getTime ();
                    String[] endHourMinSec = startEndTime[1].split (":");
                    cal.set (Calendar.HOUR_OF_DAY, Integer.valueOf (endHourMinSec[0]));
                    cal.set (Calendar.MINUTE, Integer.valueOf (endHourMinSec[1]));
                    if (endHourMinSec.length < 3){
                        cal.set (Calendar.SECOND, 0);
                    }else{
                        cal.set (Calendar.SECOND, Integer.valueOf (endHourMinSec[2]));
                    }
                    intervalDateEnd = cal.getTime ();
                    dayIntervals.add (new Interval (intervalDateStart, intervalDateEnd));
                } catch ( NumberFormatException e ) {
                    throw new ParseException (startEndTime[0], startEndTime[0].length ());
                }
            } else {
                throw new ParseException (dateTime[1], dateTime[1].length ());
            }
        }
    }

    public List<Interval> getDayIntervals () {
        return new ArrayList<Interval> (dayIntervals);
    }

    public boolean contains(Date date){
        for ( Interval interval : dayIntervals ){
            if (interval.contains (date)){
                return true;
            }
        }
        return false;
    }

}
