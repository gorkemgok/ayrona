package com.ayronasystems.core.timeseries.moment;

import java.util.Date;
import java.util.NoSuchElementException;

/**
 * Created by gorkemgok on 21/06/15.
 */
public class SignalMoment extends AbstractMoment {

    public static final ColumnType[] COLUMN_TYPES = new ColumnType[]{ColumnType.SIGNAL};

    public static final ColumnDefinition[] COLUMN_DEFINTIONS = new ColumnDefinition[]{
            new ColumnDefinition (ColumnType.SIGNAL)
    };

    public static final int COLUMN_COUNT = 1;

    public enum SignalType {
        HOLD (0),
        BUY (1),
        SELL (2),
        BOTH(3),
        NOTCALCULATED (4),
        ILLEGAL (5);

        private int intValue;

        SignalType (int intValue) {
            this.intValue = intValue;
        }

        public int getIntValue () {
            return intValue;
        }
    }

    private SignalType signal;

    public SignalMoment (Date timestamp) {
        super (timestamp);
        this.signal = SignalType.NOTCALCULATED;
    }

    public SignalMoment (Date timestamp, SignalType signal) {
        super (timestamp);
        this.signal = signal;
    }

    public SignalType getSignal () {
        return signal;
    }

    public void setSignal (SignalType signal) {
        this.signal = signal;
    }

    public double get(ColumnDefinition columnDefinition) {
        ColumnType columnType = columnDefinition.getColumnType();
        switch (columnType) {
            case SIGNAL:
                return signal.getIntValue();
            default:
                throw new NoSuchElementException(String.format("ColumnType %s is illegal for Signal", columnType));
        }
    }

    public void set(ColumnDefinition columnDefinition, double value) {
        ColumnType columnType = columnDefinition.getColumnType();
        switch (columnType) {
            case SIGNAL:
                set(0, (int)value);
                break;
            default:
                throw new NoSuchElementException(String.format("ColumnType %s is illegal for Signal", columnType));
        }
    }

    public int getIndex(ColumnDefinition columnDefinition) {
        ColumnType columnType = columnDefinition.getColumnType();
        switch (columnType) {
            case SIGNAL:
                return columnType.getIndex();
            default:
                throw new NoSuchElementException(String.format("ColumnType %s is illegal for Signal", columnType));
        }
    }

    public double get(int index) {
        switch (index) {
            case 0:
                return signal.getIntValue();
            default:
                throw new IndexOutOfBoundsException(String.format("Out of index Signal : %d", index));
        }
    }

    public void set(int index, double value) {
        switch (index) {
            case 0:
                switch ((int)value){
                    case 0:
                        signal = SignalType.HOLD;
                        break;
                    case 1:
                        signal = SignalType.BUY;
                        break;
                    case 2:
                        signal = SignalType.SELL;
                        break;
                    case 3:
                        signal = SignalType.BOTH;
                        break;
                    case 4:
                        signal = SignalType.NOTCALCULATED;
                        break;
                    default:
                        signal = SignalType.ILLEGAL;
                }
                break;
            default:
                throw new IndexOutOfBoundsException(String.format("Out of index Signal : %d", index));
        }
    }

    @Override
    public boolean hasValue(ColumnDefinition columnDefinition) {
        if (columnDefinition.getColumnType() == ColumnType.SIGNAL) {
            return signal != SignalType.NOTCALCULATED;
        }
        return false;
    }

    @Override
    public boolean hasValue(int index) {
        return signal != SignalType.NOTCALCULATED;
    }

    public boolean hasNotCalculatedValue() {
        return signal == SignalType.NOTCALCULATED;
    }

    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    public ColumnDefinition[] getColumnDefinition () {
        return COLUMN_DEFINTIONS;
    }

    public static SignalType parseSignal(int index){
        switch (index){
            case 0:
                return SignalType.HOLD;
            case 1:
                return SignalType.BUY;
            case 2:
                return SignalType.SELL;
            case 3:
                return SignalType.BOTH;
            case 4:
                return SignalType.NOTCALCULATED;
            default:
                return SignalType.ILLEGAL;
        }
    }

    public static SignalType parse (double index){
        return parseSignal ((int)index);
    }
}
