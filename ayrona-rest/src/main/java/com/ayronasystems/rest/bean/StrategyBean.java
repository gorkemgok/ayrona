package com.ayronasystems.rest.bean;

import com.ayronasystems.core.dao.model.AccountBinder;
import com.ayronasystems.core.dao.model.StrategyModel;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by gorkemgok on 06/06/16.
 */
@XmlRootElement
public class StrategyBean {

    private String id;

    private String name;

    private String code;

    private String state;

    private String symbol;

    private String period;

    private int initialBarCount;

    public int getInitialBarCount() {
        return initialBarCount;
    }

    public void setInitialBarCount(int initialBarCount) {
        this.initialBarCount = initialBarCount;
    }

    public String getSymbol () {
        return symbol;
    }

    public void setSymbol (String symbol) {
        this.symbol = symbol;
    }

    public String getPeriod () {
        return period;
    }

    public void setPeriod (String period) {
        this.period = period;
    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getCode () {
        return code;
    }

    public void setCode (String code) {
        this.code = code;
    }

    public String getState () {
        return state;
    }

    public void setState (String state) {
        this.state = state;
    }

    public static StrategyBean valueOf(StrategyModel strategyModel){
        StrategyBean strategyBean = new StrategyBean ();
        strategyBean.setId (strategyModel.getId ());
        strategyBean.setName (strategyModel.getName ());
        strategyBean.setCode (strategyModel.getCode ());
        strategyBean.setSymbol (strategyModel.getSymbol ().toString ());
        strategyBean.setPeriod (strategyModel.getPeriod ().toString ());
        strategyBean.setState (strategyModel.getState ().toString ());
        strategyBean.setInitialBarCount(strategyModel.getInitialBarCount());
        return strategyBean;
    }

    public StrategyModel toStrategyModel(){
        StrategyModel strategyModel = new StrategyModel ();
        strategyModel.setId (id);
        strategyModel.setName (name);
        strategyModel.setCode (code);
        strategyModel.setSymbol (Symbol.valueOf (symbol));
        strategyModel.setPeriod (Period.valueOf (period));
        strategyModel.setState (AccountBinder.State.valueOf (state));
        strategyModel.setInitialBarCount(initialBarCount);
        return strategyModel;
    }
}
