package com.ayronasystems.core.backtest;

import com.ayronasystems.core.util.DateUtils;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 29/08/16.
 */
public class PeriodicAggregatorTest {

    @Test
    public void aggregateDailyResult(){
        Date[] datesDay = new Date[]{
                DateUtils.parseDate ("01.01.2015 01:00:00"),
                DateUtils.parseDate ("01.01.2015 02:00:00"),
                DateUtils.parseDate ("02.01.2015 01:00:00"),
                DateUtils.parseDate ("02.01.2015 02:00:00"),
                DateUtils.parseDate ("03.01.2015 01:00:00"),
                DateUtils.parseDate ("04.01.2015 02:00:00"),
                DateUtils.parseDate ("04.01.2015 03:00:00")
        };
        double[] profitsDays = new double[]{3,-2,5,-6,-1,-2, 4};
        double[] profitsPDays = new double[]{.3,-.2,.5,-.6,-.1,-.2, .4};

        PeriodicAggregator pa = new PeriodicAggregator (ResultPeriod.DAY);
        for ( int i = 0; i < datesDay.length; i++ ) {
            pa.forwardDate (datesDay[i]);
            pa.addProfit (profitsDays[i], profitsPDays[i]);
        }
        pa.endPeriod ();

        List<ResultQuanta> resultList = pa.getResultList ();
        resultList.size ();
    }

}