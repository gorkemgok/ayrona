package com.ayronasystems.core.edr;

import com.ayronasystems.core.strategy.Position;
import com.ayronasystems.core.definition.Signal;
import com.ayronasystems.core.definition.Symbol;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gorkemg on 10.06.2016.
 */
public class Edr {

    private static EdrLogger edrLogger = EdrLogger.getLogger();

    private static EdrQueue edrQueue = EdrQueue.getQueue();

    private Date createDate;

    private EdrType type;

    EdrStatus status;

    private Map<String, String> properties = new HashMap<String, String>();

    public static class Builder{

        private Edr edr;

        public Builder(EdrType edrType) {
            edr = new Edr();
            edr.createDate = new Date();
            edr.type = edrType;
        }

        public Builder error(){
            edr.status = EdrStatus.FAILED;
            return this;
        }

        public Builder success(){
            edr.status = EdrStatus.SUCCESSFUL;
            return this;
        }

        public Builder signal(Symbol symbol, List<Signal> signalList){
            edr.properties.put("symbol", symbol.getName ());
            edr.properties.put("signal", signalList.get(0).toString());
            return this;
        }

        public Builder strategy(String name){
            edr.properties.put("strategy", name);
            return this;
        }

        public Builder account(String name){
            edr.properties.put("account", name);
            return this;
        }

        public Builder strategyId(String id){
            edr.properties.put("strategyId", id);
            return this;
        }

        public Builder accountId(String id){
            edr.properties.put("accountId", id);
            return this;
        }

        public Builder lot(double lot){
            edr.properties.put("lot", String.valueOf(lot));
            return this;
        }

        public Builder position(Position position){
            edr.properties.put("position", position.getDirection().toString());
            edr.properties.put("position_symbol", position.getSymbol().getName ());
            //TODO: Add more info
            return this;
        }
        public void save(){
            edrLogger.save(edr);
        }

        public void putQueue(){
            edrQueue.putQueue(edr);
        }

    }
    private Edr() {
    }

    public static Builder boundAccount(){return new Builder(EdrType.BOUND_ACCOUNT);}
    public static Builder unboundAccount(){return new Builder(EdrType.UNBOUND_ACCOUNT);}
    public static Builder startStrategy(){return new Builder(EdrType.START_STRATEGY);}
    public static Builder stopStrategy(){return new Builder(EdrType.STOP_STRATEGY);}
    public static Builder newSignal(){return new Builder(EdrType.NEW_SIGNAL);}
    public static Builder startAccount(){return new Builder(EdrType.START_ACCOUNT);}
    public static Builder stopAccount(){return new Builder(EdrType.STOP_ACCOUNT);}
    public static Builder newPosition(){return new Builder(EdrType.NEW_POSITION);}

    public Date getCreateDate() {
        return createDate;
    }

    public EdrType getType() {
        return type;
    }

    public EdrStatus getStatus() {
        return status;
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}
