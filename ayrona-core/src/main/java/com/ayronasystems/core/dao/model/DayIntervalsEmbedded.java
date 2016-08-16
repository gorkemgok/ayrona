package com.ayronasystems.core.dao.model;

import org.mongodb.morphia.annotations.Embedded;

/**
 * Created by gorkemgok on 12/08/16.
 */
@Embedded
public class DayIntervalsEmbedded {

    private boolean isOff;

    private String expression;

    private int[] excludedDays;

    public int[] getExcludedDays () {
        return excludedDays;
    }

    public void setExcludedDays (int[] excludedDays) {
        this.excludedDays = excludedDays;
    }

    public boolean isOff () {
        return isOff;
    }

    public void setOff (boolean off) {
        isOff = off;
    }

    public String getExpression () {
        return expression;
    }

    public void setExpression (String expression) {
        this.expression = expression;
    }
}
