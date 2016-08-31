package com.ayronasystems.core.util.date;

import java.util.Date;

/**
 * Created by gorkemgok on 29/08/16.
 */
public class InfinityComparator implements DatePeriodComparator{

    public boolean isSamePeriod (Date date1, Date date2) {
        return date1 != null && date2 != null;
    }
}
