package com.ayronasystems.core.util.date;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by gorkemgok on 29/08/16.
 */
public class DayComparator implements DatePeriodComparator{

    private Calendar cal1 = Calendar.getInstance ();

    private Calendar cal2 = Calendar.getInstance ();

    public boolean isSamePeriod (Date date1, Date date2) {
        if (date1 == null || date2 == null){
            return false;
        }
        cal1.setTime (date1);
        cal2.setTime (date2);
        return  cal1.get (Calendar.YEAR) == cal2.get (Calendar.YEAR) &&
                cal1.get (Calendar.DAY_OF_YEAR) == cal2.get (Calendar.DAY_OF_YEAR);
    }

}
