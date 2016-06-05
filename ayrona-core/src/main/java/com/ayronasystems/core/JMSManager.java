package com.ayronasystems.core;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gorkemgok on 21/01/16.
 */
public class JMSManager {

    public static final String TICK4J_BARS = "tick4j.bars";

    public static final String TICK4J_SELECTED_STRATEGIES = "tick4j.strategies.selected";

    public static final String TICK4J_START_TRAINING = "tick4j.start.training";

    private static Map<String, JMSManager> managers = new HashMap<String, JMSManager> ();

    public class Worker {

        private JMSDestination destination;

        private String address;

        private JMSManager manager;

        private Worker (JMSManager manager) {
            this.manager = manager;
        }

        public Worker destination (JMSDestination destination) {
            this.destination = destination;
            return this;
        }

        public Worker address (String address) {
            this.address = address;
            return  this;
        }

        public void send(Serializable message) throws JMSException {
            ObjectMessage objectMessage = manager.getSession ().createObjectMessage (message);
            manager.getProducer (destination, address).send (objectMessage);
        }

        public void register(MessageListener messageListener) throws JMSException {
            manager.getConsumer (destination, address).setMessageListener (messageListener);
        }
    }

    private String uri;

    private Session session;

    private Connection jmsConnection;

    private Map<String, MessageProducer> producerMap = new HashMap<String, MessageProducer> ();

    private Map<String, MessageConsumer> consumerMap = new HashMap<String, MessageConsumer> ();

    private JMSManager (String uri) throws JMSException {
        this.uri = uri;
    }

    public static JMSManager getManager (String uri) throws JMSException {
        if (managers.containsKey (uri)){
            return managers.get (uri);
        }
        JMSManager manager = new JMSManager (uri);
        managers.put (uri, manager);
        return manager;
    }

    public MessageProducer getProducer (JMSDestination JMSDestination, String address) throws JMSException {
        String fullDestinationName = JMSDestination.getName ()+(address != null? "."+address : "");
        if (producerMap.containsKey (fullDestinationName)){
            return producerMap.get (fullDestinationName);
        }
        MessageProducer newProducer = getSession ().createProducer (JMSDestination.newDestinationInstance (getSession (), address));
        producerMap.put (fullDestinationName, newProducer);
        return newProducer;
    }

    public MessageConsumer getConsumer (JMSDestination JMSDestination, String address) throws JMSException {
        String fullDestinationName = JMSDestination.getName ()+(address != null? "."+address : "");
        if (consumerMap.containsKey (fullDestinationName)){
            return consumerMap.get (fullDestinationName);
        }
        MessageConsumer newConsumer = getSession ().createConsumer (JMSDestination.newDestinationInstance (getSession (), address));
        consumerMap.put (fullDestinationName, newConsumer);
        return newConsumer;
    }

    public Worker getWorker (){
        return new Worker (this);
    }

    private Session getSession() throws JMSException {
        if (session != null){
            return session;
        }
        return initSession ();
    }

    private Session initSession() throws JMSException {
        ConnectionFactory jmsConnectionFactory = new ActiveMQConnectionFactory (uri);
        jmsConnection = jmsConnectionFactory.createConnection ();
        jmsConnection.start ();
        session = jmsConnection.createSession (false, Session.AUTO_ACKNOWLEDGE);
        return session;
    }

    public void close() throws JMSException {
        jmsConnection.close ();
    }

}
