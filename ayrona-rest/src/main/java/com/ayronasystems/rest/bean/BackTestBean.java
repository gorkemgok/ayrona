package com.ayronasystems.rest.bean;

/**
 * Created by gorkemgok on 14/06/16.
 */
public class BackTestBean {

    private String symbol;

    private String period;

    private String code;

    private String beginDate;

    private String endDate;

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

    public String getCode () {
        return code;
    }

    public void setCode (String code) {
        this.code = code;
    }

    public String getBeginDate () {
        return beginDate;
    }

    public void setBeginDate (String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate () {
        return endDate;
    }

    public void setEndDate (String endDate) {
        this.endDate = endDate;
    }
}
