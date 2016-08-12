package com.ayronasystems.core.dao.model;

import org.mongodb.morphia.annotations.Indexed;

import java.util.List;

/**
 * Created by gorkemgok on 09/08/16.
 */
public class MarketCalendarModel extends BaseModel{

    @Indexed(name = "name", unique = true)
    private String name;

    private List<String> includedExpression;

}
