package com.ayronasystems.data.listener;

import com.ayronasystems.core.JMSDestination;
import com.ayronasystems.core.JMSManager;
import com.ayronasystems.core.algo.LiveBar;
import com.ayronasystems.core.configuration.Configuration;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.timeseries.moment.Bar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;

/**
 * Created by gorkemgok on 07/06/16.
 */
public class BarAMQSenderListener implements BarListener {

    private static Configuration conf = Configuration.getInstance ();

    private static Logger log = LoggerFactory.getLogger (BarAMQSenderListener.class);

    private JMSManager jmsManager;

    public BarAMQSenderListener (JMSManager jmsManager) {
        this.jmsManager = jmsManager;
    }

    public void newBar (Symbol symbol, Period period, Bar bar) {
        try {
            jmsManager.getWorker ().destination (JMSDestination.BARS).send (
                    new LiveBar (symbol, period, bar)
            );
            log.info ("Added to AMQ {}, {}", symbol, bar);
        } catch ( JMSException e ) {
            log.error ("Can't send bar "+symbol+" - "+period+", "+bar+" to queue", e);
        }
    }
}
