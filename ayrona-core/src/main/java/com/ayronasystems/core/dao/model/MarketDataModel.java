package com.ayronasystems.core.dao.model;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.definition.Symbols;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.utils.IndexDirection;

import java.util.Date;

/**
 * Created by gorkemgok on 29/06/16.
 */
@Entity("marketdata")
@Indexes ({
        @Index(name = "symbolPeriod", value = "symbol, period"),
        @Index(name = "symbolPeriodDate", value = "symbol, period, periodDate", unique = true),
})
public class MarketDataModel extends BaseModel{

    private String symbol;

    private Period period;

    @Indexed(value = IndexDirection.DESC)
    private Date periodDate;

    private double open;

    private double high;

    private double low;

    private double close;

    private double volume;

    public Symbol getSymbol () {
        return Symbols.of (symbol);
    }

    public void setSymbol (Symbol symbol) {
        this.symbol = symbol.getName ();
    }

    public Period getPeriod () {
        return period;
    }

    public void setPeriod (Period period) {
        this.period = period;
    }

    public Date getPeriodDate () {
        return periodDate;
    }

    public void setPeriodDate (Date periodDate) {
        this.periodDate = periodDate;
    }

    public double getOpen () {
        return open;
    }

    public void setOpen (double open) {
        this.open = open;
    }

    public double getHigh () {
        return high;
    }

    public void setHigh (double high) {
        this.high = high;
    }

    public double getLow () {
        return low;
    }

    public void setLow (double low) {
        this.low = low;
    }

    public double getClose () {
        return close;
    }

    public void setClose (double close) {
        this.close = close;
    }

    public double getVolume () {
        return volume;
    }

    public void setVolume (double volume) {
        this.volume = volume;
    }
}
