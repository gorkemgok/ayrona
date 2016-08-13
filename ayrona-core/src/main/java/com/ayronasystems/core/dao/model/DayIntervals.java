package com.ayronasystems.core.dao.model;

import org.mongodb.morphia.annotations.Embedded;

import java.util.List;

/**
 * Created by gorkemgok on 12/08/16.
 */
@Embedded
public class DayIntervals {

    private boolean isOff;

    private List<String> expression;

    public boolean isOff () {
        return isOff;
    }

    public void setOff (boolean off) {
        isOff = off;
    }

    public List<String> getExpression () {
        return expression;
    }

    public void setExpression (List<String> expression) {
        this.expression = expression;
    }
}
