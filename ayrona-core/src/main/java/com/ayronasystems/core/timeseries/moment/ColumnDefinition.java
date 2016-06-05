package com.ayronasystems.core.timeseries.moment;

/**
 * Created by gorkemgok on 07/07/15.
 */
public class ColumnDefinition {

    public static final ColumnDefinition OPEN = new ColumnDefinition (Moment.ColumnType.OPEN);

    public static final ColumnDefinition HIGH = new ColumnDefinition (Moment.ColumnType.HIGH);

    public static final ColumnDefinition LOW = new ColumnDefinition (Moment.ColumnType.LOW);

    public static final ColumnDefinition CLOSE = new ColumnDefinition (Moment.ColumnType.CLOSE);

    public static final ColumnDefinition VOLUME = new ColumnDefinition (Moment.ColumnType.VOLUME);

    public static final ColumnDefinition ASK = new ColumnDefinition (Moment.ColumnType.ASK);

    public static final ColumnDefinition BID = new ColumnDefinition (Moment.ColumnType.BID);

    public static final ColumnDefinition MID = new ColumnDefinition (Moment.ColumnType.MID);

    public static final ColumnDefinition SIGNAL = new ColumnDefinition (Moment.ColumnType.SIGNAL);

    public static final ColumnDefinition EQUITY = new ColumnDefinition (Moment.ColumnType.EQUITY);

    public static final ColumnDefinition MDD = new ColumnDefinition (Moment.ColumnType.MDD);

    public static final ColumnDefinition INSTANTPROFIT = new ColumnDefinition (Moment.ColumnType.INSTANTPROFIT);

    private final Moment.ColumnType columnType;

    private final String columnName;

    private final double[] params;

    public ColumnDefinition (Moment.ColumnType columnType) {
        this.columnType = columnType;
        this.columnName = columnType.toString ();
        this.params = null;
    }

    public ColumnDefinition (Moment.ColumnType columnType, String columnName) {
        this.columnType = columnType;
        this.columnName = columnName;
        this.params = null;
    }

    public ColumnDefinition(Moment.ColumnType columnType, String columnName, double[] params) {
        this.columnType = columnType;
        this.columnName = columnName;
        this.params = params;
    }

    public double[] getParams() {
        return params;
    }

    public Moment.ColumnType getColumnType () {
        return columnType;
    }

    public String getColumnName () {
        return columnName;
    }
}
