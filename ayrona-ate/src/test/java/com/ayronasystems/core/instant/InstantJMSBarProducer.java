package com.ayronasystems.core.instant;

import com.ayronasystems.core.JMSDestination;
import com.ayronasystems.core.JMSManager;
import com.ayronasystems.core.algo.LiveBar;
import com.ayronasystems.core.configuration.ConfKey;
import com.ayronasystems.core.configuration.Configuration;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.service.MarketDataService;
import com.ayronasystems.core.service.StandaloneMarketDataService;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.ayronasystems.core.timeseries.moment.Moment;
import com.ayronasystems.core.util.DateUtils;

import javax.jms.JMSException;

/**
 * Created by gorkemgok on 17/06/16.
 */
public class InstantJMSBarProducer {

    private static Configuration conf = Configuration.getInstance ();

    public static void main(String[] args) throws JMSException, InterruptedException {
        JMSManager jmsManager = JMSManager.getManager (conf.getString (ConfKey.AMQ_URI));
        MarketDataService marketDataService = StandaloneMarketDataService.getInstance ();
        MarketData marketData = marketDataService.getOHLC (Symbol.VOB30, Period.M5,
                                                           DateUtils.parseDate ("01.12.2015 00:00:00"));

        for ( Moment moment : marketData){
            LiveBar liveBar = new LiveBar (Symbol.VOB30, Period.M5, (Bar)moment);
            jmsManager.getWorker ().destination (JMSDestination.BARS).send (liveBar);
            System.out.println ("Send bar:"+liveBar.getBar ());
            Thread.currentThread ().sleep (500);
        }

    }

}
