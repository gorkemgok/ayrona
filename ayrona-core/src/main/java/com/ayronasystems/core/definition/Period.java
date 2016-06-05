package com.ayronasystems.core.definition;

/**
 * Created by gorkemgok on 12/03/15.
 */
public enum Period {

    M1(1,"1M"),
    M5(5,"5M"),
    M15(15,"15M"),
    M30(30,"30M"),
    H1(60,"1H"),
    H4(4*60,"4H"),
    D1(24*60, "1D");

    private final long period;
    private final String periodString;

    Period(long period, String periodString) {
        this.period = period;
        this.periodString = periodString;
    }

    public long getAsMillis () {
        return period * 60000;
    }

    public String getPeriodString() {
        return periodString;
    }

    public static Period parse(int minutes){
        switch ( minutes ){
            case 1:
                return M1;
            case 5:
                return M5;
            case 15:
                return M15;
            case 30:
                return M30;
            case 60:
                return H1;
            case 4*60:
                return H4;
            case 24*60:
                return D1;
            default:
                return null;
        }
    }

    public static Period parse(long millis){
        switch ( (int) millis ){
            case 60000:
                return M1;
            case 5 * 60000:
                return M5;
            case 15 * 60000:
                return M15;
            case 30 * 60000:
                return M30;
            case 60 * 60000:
                return H1;
            case 4 * 60 * 60000:
                return H4;
            case 24 * 60 * 60000:
                return D1;
            default:
                return null;
        }
    }
}
