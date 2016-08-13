package com.ayronasystems.core.dao.model;

import org.mongodb.morphia.annotations.Indexed;

import java.util.List;

/**
 * Created by gorkemgok on 09/08/16.
 */
public class MarketCalendarModel extends BaseModel{

    @Indexed(name = "name", unique = true)
    private String name;

    private List<DayIntervals> includedExpression;

    private List<String> symbols;

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public List<DayIntervals> getIncludedExpression () {
        return includedExpression;
    }

    public void setIncludedExpression (List<DayIntervals> includedExpression) {
        this.includedExpression = includedExpression;
    }

    public List<String> getSymbols () {
        return symbols;
    }

    public void setSymbols (List<String> symbols) {
        this.symbols = symbols;
    }
}
