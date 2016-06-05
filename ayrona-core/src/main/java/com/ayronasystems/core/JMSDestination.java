package com.ayronasystems.core;

import org.apache.activemq.broker.region.BaseDestination;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

/**
 * Created by gorkemgok on 22/01/16.
 */
public enum JMSDestination {

    BARS(Type.TOPIC, JMSManager.TICK4J_BARS),
    SELECTED_STRATEGIES (Type.QUEUE, JMSManager.TICK4J_SELECTED_STRATEGIES),
    START_TRAINING(Type.QUEUE, JMSManager.TICK4J_START_TRAINING);

    public enum Type{
        TOPIC (org.apache.activemq.broker.region.Topic.class),
        QUEUE (org.apache.activemq.broker.region.Queue.class);

        private Class<? extends BaseDestination> classOfDestination;

        Type (Class<? extends BaseDestination> classOfDestination) {
            this.classOfDestination = classOfDestination;
        }

        public Class<? extends BaseDestination> getClassOfDestination () {
            return classOfDestination;
        }
    }

    private Type destinationType;

    private String name;

    JMSDestination (Type destinationType, String name) {
        this.destinationType = destinationType;
        this.name = name;
    }

    public Type getDestinationType () {
        return destinationType;
    }

    public String getName () {
        return name;
    }

    public String getName (String address) {
        return name + "." + address;
    }

    public Destination newDestinationInstance(Session session, String address) throws JMSException {
        if (destinationType == Type.TOPIC){
            return session.createTopic (name+(address != null ? "."+address : ""));
        }else{
            return session.createQueue (name+(address != null ? "."+address : ""));
        }
    }
}
