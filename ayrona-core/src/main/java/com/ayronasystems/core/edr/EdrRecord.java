package com.ayronasystems.core.edr;

import com.ayronasystems.core.account.Account;
import com.ayronasystems.core.definition.Signal;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.strategy.Position;
import com.ayronasystems.core.strategy.Strategy;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gorkemg on 10.06.2016.
 */
public class EdrRecord {

    private static EdrLogger edrLogger = EdrLogger.getLogger();

    private static EdrQueue edrQueue = EdrQueue.getQueue();

    private Date createDate;

    private EdrType type;

    EdrStatus status;

    private Map<String, String> properties = new HashMap<String, String>();

    public static class Builder{

        private EdrRecord edrRecord;

        public Builder(EdrType edrType) {
            edrRecord = new EdrRecord ();
            edrRecord.createDate = new Date();
            edrRecord.type = edrType;
        }

        public Builder error(){
            edrRecord.status = EdrStatus.FAILED;
            return this;
        }

        public Builder success(){
            edrRecord.status = EdrStatus.SUCCESSFUL;
            return this;
        }

        public Builder signal(Symbol symbol, List<Signal> signalList){
            edrRecord.properties.put("symbol", symbol.getName ());
            edrRecord.properties.put("signal", signalList.get(0).toString());
            return this;
        }

        public Builder strategy(Strategy strategy){
            edrRecord.properties.put("strategy", strategy.getName ());
            edrRecord.properties.put ("strategyId", strategy.getId ());
            return this;
        }


        public Builder strategyName(String name){
            edrRecord.properties.put("strategy", name);
            return this;
        }

        public Builder account(Account account){
            edrRecord.properties.put("account", account.getName ());
            edrRecord.properties.put ("accountId", account.getId ());
            return this;
        }

        public Builder accountName(String name){
            edrRecord.properties.put("account", name);
            return this;
        }

        public Builder strategyId(String id){
            edrRecord.properties.put("strategyId", id);
            return this;
        }

        public Builder accountId(String id){
            edrRecord.properties.put("accountId", id);
            return this;
        }

        public Builder lot(double lot){
            edrRecord.properties.put("lot", String.valueOf(lot));
            return this;
        }

        public Builder position(Position position){
            edrRecord.properties.put("position", position.getDirection().toString());
            edrRecord.properties.put("position_symbol", position.getSymbol().getName ());
            //TODO: Add more info
            return this;
        }
        public void save(){
            edrLogger.save(edrRecord);
        }

        public void putQueue(){
            edrQueue.putQueue(edrRecord);
        }

    }

    private EdrRecord () {
    }



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
