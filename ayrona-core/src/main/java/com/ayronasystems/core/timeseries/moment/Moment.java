package com.ayronasystems.core.timeseries.moment;

import java.util.Date;

/**
 * Created by gorkemgok on 21/06/15.
 */
public interface Moment {
    enum ColumnType{
        DATE(-1, "DATE"),
        OPEN(0, "OPEN"),
        HIGH(1, "HIGH"),
        LOW(2, "LOW"),
        CLOSE(3 ,"CLOSE"),
        VOLUME(4, "VOLUME"),
        ASK(0, "ASK"),
        BID(1, "BID"),
        MID(2, "MID"),
        INDICATORVALUE(1, "INDICATORVALUE"),
        SIGNAL(1, "SIGNAL"),
        EQUITY(0, "EQUITY"),
        MDD(1, "MAXDRAWDOWN"),
        INSTANTPROFIT(2, "INSTANTPROFIT");
        ;

        private int index;

        private String name;

        ColumnType(int index) {
            this.index = index;
        }

        ColumnType (int index, String name) {
            this.index = index;
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public String getName () {
            return name;
        }
    }

    Date getDate ();

    double get (ColumnDefinition columnDefinition);

    double get (int index);

    void set (ColumnDefinition columnDefinition, double value);

    void set (int index, double value);

    boolean hasValue (ColumnDefinition columnDefinition);

    boolean hasValue (int index);

    boolean hasNotCalculatedValue ();

    int getColumnCount ();

    ColumnDefinition[] getColumnDefinition ();

    int getIndex (ColumnDefinition columnDefinition);

}
