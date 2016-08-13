package com.ayronasystems.core.util.calendar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 09/08/16.
 */
public class MarketCalendar {

    private List<DayIntervals> dayIntervalsList = new ArrayList<DayIntervals> ();

    public boolean isMarketOpen(Date date){
        boolean isOpen = false;
        for ( DayIntervals dayIntervals : dayIntervalsList ){
            if (dayIntervals.isOff ()){
                isOpen = isOpen && !dayIntervals.contains (date);
            }else{
                isOpen = dayIntervals.contains (date);
            }
        }
        return isOpen;
    }

    public void include(String expression, int... excludeDays) throws ParseException {
        dayIntervalsList.add (
                DayIntervals.createBusinessDays (expression, excludeDays)
        );
    }

    public void exclude(String expression, int... excludeDays) throws ParseException {
        dayIntervalsList.add (
                DayIntervals.createOffDays (expression, excludeDays)
        );
    }

    public List<DayIntervals> getDayIntervalsList () {
        return new ArrayList<DayIntervals> (dayIntervalsList);
    }
}
