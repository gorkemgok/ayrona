package com.ayronasystems.core.dao.mysql.model;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.timeseries.moment.Bar;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Date;

/**
 * Created by gorkemgok on 12/03/15.
 */
@Entity
@Table(name="barmoment", uniqueConstraints = @UniqueConstraint(columnNames = {"tick_date", "symbol", "period"}))
public class BarModel extends SymbolModel {

    private double open;

    private double high;

    private double low;

    private double close;

    private double volume;

    public BarModel(){
        super();
    }

    public BarModel(Date tickDate, Symbol symbol, Period period, double high, double open, double close, double low, double volume) {
        super(tickDate, symbol, period);
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }

    public double getVolume() {
        return volume;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public Bar convertToMoment(){
        return new Bar(getTickDate(), open, high, low, close, volume);
    }

}
