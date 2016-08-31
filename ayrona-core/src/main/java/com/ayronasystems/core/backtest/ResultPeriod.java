package com.ayronasystems.core.backtest;

import com.ayronasystems.core.util.date.*;

/**
 * Created by gorkemgok on 29/08/16.
 */
public enum ResultPeriod {
    DAY(null),
    WEEK(DAY),
    MONTH(DAY),
    YEAR(MONTH),
    INFINITY(YEAR);

    private ResultPeriod subPeriod;

    ResultPeriod (ResultPeriod subPeriod) {
        this.subPeriod = subPeriod;
    }

    public ResultPeriod getSubPeriod () {
        return subPeriod;
    }

    public DatePeriodComparator generateComparator(){
        switch ( this ){
            case DAY:
                return new DayComparator ();
            case WEEK:
                return new WeekComparator ();
            case MONTH:
                return new MonthComparator ();
            case YEAR:
                return new YearComparator ();
            case INFINITY:
                return new InfinityComparator ();
            default:
                return new DayComparator ();
        }
    }

}
