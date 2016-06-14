package com.ayronasystems.core.service;

import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.timeseries.moment.Moment;
import com.ayronasystems.core.util.DateUtils;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Created by gorkemgok on 15/06/16.
 */
public class StandaloneMarketDataServiceTestITCase {

    @Test
    public void getOHLC () throws Exception {
        MarketDataService mds = StandaloneMarketDataService.getInstance ();
        Date beginDate = DateUtils.parseDate ("01.01.2015 00:00:00");
        Date endDate = DateUtils.parseDate ("01.01.2016 00:00:00");
        MarketData ohlc = mds.getOHLC (Symbol.VOB30, Period.M5,
                                       beginDate,
                                       endDate);

        assertTrue(beginDate.before (ohlc.getBeginningDate ()) || beginDate.equals (ohlc.getBeginningDate ()));
        Date lastDate = null;
        for (Moment moment : ohlc){
            Date date = moment.getDate ();
            if (lastDate != null){
                long diff = date.getTime () - lastDate.getTime ();
                assertTrue (diff >= Period.M5.getAsMillis ());
            }
            lastDate = date;
        }

    }

}