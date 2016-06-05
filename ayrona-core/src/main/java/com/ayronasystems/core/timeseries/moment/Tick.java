package com.ayronasystems.core.timeseries.moment;

import com.ayronasystems.core.definition.Symbol;

import java.util.Date;
import java.util.NoSuchElementException;

/**
 * Created by gorkemgok on 21/06/15.
 */
public class Tick extends AbstractMoment {

    public static final ColumnType[] COLUMN_TYPES = new ColumnType[]{ColumnType.ASK, ColumnType.MID, ColumnType.BID};

    public static final ColumnDefinition[] COLUMN_DEFINTIONS = new ColumnDefinition[]{
            new ColumnDefinition (ColumnType.ASK),
            new ColumnDefinition (ColumnType.MID),
            new ColumnDefinition (ColumnType.BID),
    };

    public static final int COLUMN_COUNT = 3;

    private Symbol symbol;

    private double ask;

    private double mid;

    private double bid;

    public Tick (Date timestamp, Symbol symbol, double ask, double mid, double bid) {
        super (timestamp);
        this.symbol = symbol;
        this.ask = ask;
        this.mid = mid;
        this.bid = bid;
    }

    public Symbol getSymbol () {
        return symbol;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public double getMid() {
        return mid;
    }

    public void setMid(double mid) {
        this.mid = mid;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double get(ColumnDefinition columnDefinition) {
        ColumnType columnType = columnDefinition.getColumnType();
        switch (columnType) {
            case ASK:
                return ask;
            case MID:
                return mid;
            case BID:
                return bid;
            default:
                throw new NoSuchElementException(String.format("ColumnType %s is illegal for Tick", columnType));
        }
    }

    public void set(ColumnDefinition columnDefinition, double value) {
        ColumnType columnType = columnDefinition.getColumnType();
        switch (columnType) {
            case ASK:
                ask = value;
                break;
            case MID:
                mid = value;
                break;
            case BID:
                bid = value;
                break;
            default:
                throw new NoSuchElementException(String.format("ColumnType %s is illegal for Tick", columnType));
        }
    }

    public int getIndex(ColumnDefinition columnDefinition) {
        ColumnType columnType = columnDefinition.getColumnType();
        switch (columnType) {
            case ASK:
                return columnType.getIndex();
            case MID:
                return columnType.getIndex();
            case BID:
                return columnType.getIndex();
            default:
                throw new NoSuchElementException(String.format("ColumnType %s is illegal for Tick", columnType));
        }
    }

    public double get(int index) {
        switch (index) {
            case 0:
                return ask;
            case 1:
                return mid;
            case 2:
                return bid;
            default:
                throw new IndexOutOfBoundsException(String.format("Out of index Tick : %d", index));
        }
    }

    public void set(int index, double value) {
        switch (index) {
            case 0:
                ask = value;
                break;
            case 1:
                mid = value;
                break;
            case 2:
                bid = value;
                break;
            default:
                throw new IndexOutOfBoundsException(String.format("Out of index Tick : %d", index));
        }
    }

    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    public ColumnDefinition[] getColumnDefinition () {
        return COLUMN_DEFINTIONS;
    }

    public boolean hasValue(int index) {
        return true;
    }

    public boolean hasNotCalculatedValue() {
        return false;
    }

    @Override
    public boolean hasValue(ColumnDefinition columnDefiniton) {
        return false;
    }
}
