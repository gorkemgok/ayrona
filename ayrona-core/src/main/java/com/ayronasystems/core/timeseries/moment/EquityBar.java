package com.ayronasystems.core.timeseries.moment;

import com.ayronasystems.core.util.DateUtils;

import java.util.Date;
import java.util.NoSuchElementException;

/**
 * Created by gorkemgok on 10/01/16.
 */
public class EquityBar extends AbstractMoment {

    public static final ColumnDefinition[] COLUMN_DEFINITIONS = new ColumnDefinition[]{
            ColumnDefinition.EQUITY,
            ColumnDefinition.MDD,
            ColumnDefinition.INSTANTPROFIT
    };

    private double equity;

    private double mdd;

    private double instantProfit;

    public EquityBar () {
        super ();
    }

    public EquityBar (Date timestamp, double equity, double mdd, double instantProfit) {
        super (timestamp);
        this.equity = equity;
        this.mdd = mdd;
        this.instantProfit = instantProfit;
    }

    public double getEquity () {
        return equity;
    }

    public void setEquity (double equity) {
        this.equity = equity;
    }

    public double getMdd () {
        return mdd;
    }

    public void setMdd (double mdd) {
        this.mdd = mdd;
    }

    public double getInstantProfit () {
        return instantProfit;
    }

    public void setInstantProfit (double instantProfit) {
        this.instantProfit = instantProfit;
    }

    public double get (ColumnDefinition columnDefinition) {
        ColumnType columnType = columnDefinition.getColumnType ();
        switch (columnType) {
            case EQUITY:
                return equity;
            case MDD:
                return mdd;
            case INSTANTPROFIT:
                return instantProfit;
            default:
                throw new NoSuchElementException (String.format("ColumnType %s is illegal for Equity Bar", columnType));
        }
    }

    public double get (int index) {
        switch (index) {
            case 0:
                return equity;
            case 1:
                return mdd;
            case 2:
                return instantProfit;
            default:
                throw new NoSuchElementException(String.format("Out of index Equity Bar : %d", index));
        }
    }

    public void set (ColumnDefinition columnDefinition, double value) {
        ColumnType columnType = columnDefinition.getColumnType();
        switch (columnType) {
            case EQUITY:
                equity = value;
                break;
            case MDD:
                mdd = value;
                break;
            case INSTANTPROFIT:
                instantProfit = value;
                break;
            default:
                throw new NoSuchElementException(String.format("ColumnType %s is illegal for Equity Bar", columnType));
        }
    }

    public void set (int index, double value) {
        switch (index) {
            case 0:
                equity = value;
                break;
            case 1:
                mdd = value;
                break;
            case 2:
                instantProfit = value;
                break;
            default:
                throw new NoSuchElementException(String.format("Out of index Equity Bar : %d", index));
        }
    }

    public boolean hasNotCalculatedValue () {
        return false;
    }

    public int getColumnCount () {
        return COLUMN_DEFINITIONS.length;
    }

    public ColumnDefinition[] getColumnDefinition () {
        return COLUMN_DEFINITIONS;
    }

    public int getIndex (ColumnDefinition columnDefinition) {
        ColumnType columnType = columnDefinition.getColumnType();
        switch (columnType) {
            case EQUITY:
                return columnType.getIndex();
            case MDD:
                return columnType.getIndex();
            case INSTANTPROFIT:
                return columnType.getIndex ();
            default:
                throw new NoSuchElementException(String.format("ColumnType %s is illegal for Equity Bar", columnType));
        }
    }

    @Override
    public String toString () {
        StringBuilder stringBuilder = new StringBuilder (DateUtils.formatDate (getDate ())).append ("\t")
                                                                                           .append (equity).append ("\t")
                                                                                           .append (mdd).append ("\t")
                                                                                           .append (instantProfit).append ("\t");

        return stringBuilder.toString ();
    }
}
