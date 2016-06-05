package com.ayronasystems.core.timeseries.moment;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.NoSuchElementException;

/**
 * Created by gorkemgok on 21/06/15.
 */
public abstract class AbstractMoment implements Moment, Serializable{

    protected Date timestamp;

    public AbstractMoment () {
    }

    public AbstractMoment(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getDate() {
        return timestamp;
    }

    public boolean hasValue(int index) {
        if (index > getColumnCount () - 1){
            throw new NoSuchElementException (String.format("Out of index Bar : %d", index));
        }
        return true;
    }

    public boolean hasValue(ColumnDefinition columnDefinition) {
        try{
            get (columnDefinition);
        }catch ( NoSuchElementException ex ){
            throw ex;
        }
        return true;
    }

    @Override
    public String toString () {
        ColumnDefinition[] columnDefinitions = this.getColumnDefinition ();
        StringBuilder stringBuilder = new StringBuilder ().append (String.format ("%30s", this.getDate ()))
                     .append ("\t");
        for (ColumnDefinition columnDefinition : columnDefinitions ) {
            String value;
            if (columnDefinition.getColumnType() == Moment.ColumnType.SIGNAL){
                int mv = (int)this.get (columnDefinition);
                switch (mv){
                    case 0:
                        value = "HOLD";
                        break;
                    case 1:
                        value = "BUY";
                        break;
                    case 2:
                        value = "SELL";
                        break;
                    case 3:
                        value = "BOTH";
                        break;
                    case 4:
                        value = "NOTCALC";
                        break;
                    default:
                        value = "ILLEGAL";
                }
            }else{
                value = new DecimalFormat ("0.0000").format(this.get (columnDefinition));
            }
            stringBuilder.append (String.format ("%14s", this.hasValue (columnDefinition) ? value:"NaN"))
                         .append ("\t");
        }
        return stringBuilder.toString ();
    }
}
