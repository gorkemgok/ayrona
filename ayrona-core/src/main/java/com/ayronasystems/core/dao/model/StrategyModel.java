package com.ayronasystems.core.dao.model;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import org.mongodb.morphia.annotations.Entity;

import java.util.List;

/**
 * Created by gorkemgok on 26/05/16.
 */
@Entity("strategy")
public class StrategyModel extends BaseModel {

    private Symbol symbol;

    private Period period;

    private String name;

    private String code;

    private AccountBinder.State state;

    private List<AccountBinder> boundAccounts;

    public Symbol getSymbol () {
        return symbol;
    }

    public void setSymbol (Symbol symbol) {
        this.symbol = symbol;
    }

    public Period getPeriod () {
        return period;
    }

    public void setPeriod (Period period) {
        this.period = period;
    }

    public List<AccountBinder> getBoundAccounts () {
        return boundAccounts;
    }

    public void setBoundAccounts (List<AccountBinder> boundAccounts) {
        this.boundAccounts = boundAccounts;
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

    public AccountBinder.State getState () {
        return state;
    }

    public void setState (AccountBinder.State state) {
        this.state = state;
    }
}



