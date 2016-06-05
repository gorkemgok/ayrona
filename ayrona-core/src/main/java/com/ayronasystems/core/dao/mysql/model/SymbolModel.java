package com.ayronasystems.core.dao.mysql.model;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * Created by gorkemg on 13.07.2015.
 */
@MappedSuperclass
public class SymbolModel extends BaseModel {

    private Symbol symbol;

    private Period period;

    private Date tickDate;

    public SymbolModel() {
    }

    public SymbolModel(Date tickDate, Symbol symbol, Period period) {
        this.symbol = symbol;
        this.period = period;
        this.tickDate = tickDate;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "symbol",nullable = false)
    public Symbol getSymbol() {
        return symbol;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "period", nullable = false)
    public Period getPeriod() {
        return period;
    }

    @Column(name = "tick_date", nullable = false)
    public Date getTickDate() {
        return tickDate;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public void setTickDate(Date tickDate) {
        this.tickDate = tickDate;
    }
}
