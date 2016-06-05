package com.ayronasystems.core.timeseries.moment;

import com.ayronasystems.core.util.DateUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.NoSuchElementException;

/**
 * Created by gorkemgok on 21/06/15.
 */
public class Bar extends AbstractMoment implements Serializable{

    public static final ColumnDefinition[] COLUMN_DEFINITIONS = new ColumnDefinition[]{
            new ColumnDefinition (ColumnType.OPEN),
            new ColumnDefinition (ColumnType.HIGH),
            new ColumnDefinition (ColumnType.LOW),
            new ColumnDefinition (ColumnType.CLOSE),
            new ColumnDefinition (ColumnType.VOLUME)
    };

    public static final int COLUMN_COUNT = 5;

    private double high;

    private double open;

    private double close;

    private double low;

    private double volume;

    public Bar () {
        super ();
    }

    public Bar (Date timestamp, double open, double high, double low, double close, double volume) {
        super (timestamp);
        this.high = high;
        this.open = open;
        this.close = close;
        this.low = low;
        this.volume = volume;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double get(ColumnDefinition columnDefinition) {
        ColumnType columnType = columnDefinition.getColumnType();
        switch (columnType) {
            case HIGH:
                return high;
            case OPEN:
                return open;
            case CLOSE:
                return close;
            case LOW:
                return low;
            case VOLUME:
                return volume;
            default:
                throw new NoSuchElementException(String.format("ColumnType %s is illegal for Bar", columnType));
        }
    }

    public void set(ColumnDefinition columnDefinition, double value) {
        ColumnType columnType = columnDefinition.getColumnType();
        switch (columnType) {
            case HIGH:
                high = value;
                break;
            case OPEN:
                open = value;
                break;
            case CLOSE:
                close = value;
                break;
            case LOW:
                low = value;
                break;
            case VOLUME:
                volume = value;
                break;
            default:
                throw new NoSuchElementException(String.format("ColumnType %s is illegal for Bar", columnType));
        }
    }

    public int getIndex(ColumnDefinition columnDefinition) {
        ColumnType columnType = columnDefinition.getColumnType();
        switch (columnType) {
            case HIGH:
                return columnType.getIndex();
            case OPEN:
                return columnType.getIndex();
            case CLOSE:
                return columnType.getIndex();
            case LOW:
                return columnType.getIndex();
            case VOLUME:
                return columnType.getIndex();
            default:
                throw new NoSuchElementException(String.format("ColumnType %s is illegal for Bar", columnType));
        }
    }

    public double get(int index) {
        switch (index) {
            case 0:
                return open;
            case 1:
                return high;
            case 2:
                return low;
            case 3:
                return close;
            case 4:
                return volume;
            default:
                throw new NoSuchElementException(String.format("Out of index Bar : %d", index));
        }
    }

    public void set(int index, double value) {
        switch (index) {
            case 0:
                open = value;
                break;
            case 1:
                high = value;
                break;
            case 2:
                low = value;
                break;
            case 3:
                close = value;
                break;
            case 4:
                volume = value;
                break;
            default:
                throw new NoSuchElementException(String.format("Out of index Bar : %d", index));
        }
    }

    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    public ColumnDefinition[] getColumnDefinition () {
        return COLUMN_DEFINITIONS;
    }

    public boolean hasNotCalculatedValue() {
        return false;
    }

    @Override
    public String toString () {
        StringBuilder stringBuilder = new StringBuilder (DateUtils.formatDate (getDate ())).append (" ")
                                                                                           .append (open).append (" ")
                                                                                           .append (high).append (" ")
                                                                                           .append (low).append (" ")
                                                                                           .append (close).append (" ")
                                                                                           .append (volume).append (" ");
        return stringBuilder.toString ();
    }
}
