package com.ayronasystems.core.timeseries.moment;

import java.util.NoSuchElementException;

/**
 * Created by gorkemgok on 28/06/15.
 */
public class StrategyMoment<M extends Moment> extends AbstractMoment {

    private M baseMoment;

    private IndicatorMoment indicatorMoment;

    private SignalMoment signalMoment;

    public StrategyMoment(M baseMoment, IndicatorMoment indicatorMoment, SignalMoment signalMoment) {
        super(baseMoment.getDate());
        this.baseMoment = baseMoment;
        this.indicatorMoment = indicatorMoment;
        this.signalMoment = signalMoment;
    }

    public StrategyMoment () {
    }

    public void setBaseMoment (M baseMoment) {
        timestamp = baseMoment.getDate ();
        this.baseMoment = baseMoment;
    }

    public void setIndicatorMoment (IndicatorMoment indicatorMoment) {
        this.indicatorMoment = indicatorMoment;
    }

    public void setSignalMoment (SignalMoment signalMoment) {
        this.signalMoment = signalMoment;
    }

    public M getBaseMoment() {
        return baseMoment;
    }

    public IndicatorMoment getIndicatorMoment() {
        return indicatorMoment;
    }

    public SignalMoment getSignalMoment() {
        return signalMoment;
    }

    public double get(ColumnDefinition columnDefinition) {
        try{
            return baseMoment.get(columnDefinition);
        }catch (NoSuchElementException e1){
            try{
                return  indicatorMoment.get(columnDefinition);
            }catch (NoSuchElementException e2){
                try {
                    return signalMoment.get(columnDefinition);
                }catch (NoSuchElementException e3){
                    throw new NoSuchElementException(
                              String.format("There is no column for StrategySeries with name : %s",columnDefinition.getColumnName()));
                }
            }
        }
    }

    public void set(ColumnDefinition columnDefinition, double value) {
        try{
            baseMoment.set(columnDefinition, value);
        }catch (NoSuchElementException e1){
            try{
                indicatorMoment.set(columnDefinition, value);
            }catch (NoSuchElementException e2){
                try {
                    signalMoment.set(columnDefinition, value);
                }catch (NoSuchElementException e3){
                    throw new NoSuchElementException(
                            String.format("There is no column for StrategySeries with name : %s",columnDefinition.getColumnName()));
                }
            }
        }
    }

    public int getIndex(ColumnDefinition columnDefinition) {
        try {
            return baseMoment.getIndex(columnDefinition);
        } catch (NoSuchElementException e1) {
            try {
                return baseMoment.getColumnCount() + indicatorMoment.getIndex(columnDefinition);
            } catch (NoSuchElementException e2) {
                try {
                    return baseMoment.getColumnCount() + indicatorMoment.getColumnCount() + signalMoment.getIndex(columnDefinition);
                } catch (NoSuchElementException e3) {
                    throw new NoSuchElementException(
                            String.format("There is no column for StrategySeries with name : %s", columnDefinition.getColumnName()));
                }
            }
        }
    }

    public double get(int index) {
        if (index < baseMoment.getColumnCount()){
            return baseMoment.get(index);
        }else{
            index -= baseMoment.getColumnCount();
            if (index < indicatorMoment.getColumnCount()){
                return indicatorMoment.get(index);
            }else{
                index -= indicatorMoment.getColumnCount();
                if (index < signalMoment.getColumnCount()){
                    return signalMoment.get(index);
                }else{
                    throw new NoSuchElementException(
                            String.format("No element in StrategySeries at position %d", index));
                }
            }
        }
    }

    public void set(int index, double value) {
        if (index < baseMoment.getColumnCount()){
            baseMoment.set(index, value);
        }else{
            index -= baseMoment.getColumnCount();
            if (index < indicatorMoment.getColumnCount()){
                indicatorMoment.set(index, value);
            }else{
                index -= indicatorMoment.getColumnCount();
                if (index < signalMoment.getColumnCount()){
                    signalMoment.set(index, value);
                }else{
                    throw new NoSuchElementException(
                            String.format("No element in StrategySeries at position %d", index));
                }
            }
        }
    }

    public boolean hasNotCalculatedValue() {
        return baseMoment.hasNotCalculatedValue() || indicatorMoment.hasNotCalculatedValue();
    }

    @Override
    public boolean hasValue (ColumnDefinition columnDefinition) {
        try{
            return baseMoment.hasValue (columnDefinition);
        }catch (NoSuchElementException e1){
            try{
                return  indicatorMoment.hasValue (columnDefinition);
            }catch (NoSuchElementException e2){
                try {
                    return signalMoment.hasValue (columnDefinition);
                }catch (NoSuchElementException e3){
                    throw new NoSuchElementException(
                            String.format("There is no column for StrategySeries with name : %s",columnDefinition.getColumnName()));
                }
            }
        }
    }

    @Override
    public boolean hasValue (int index) {
        if (index < baseMoment.getColumnCount()){
            return baseMoment.hasValue (index);
        }else{
            index -= baseMoment.getColumnCount();
            if (index < indicatorMoment.getColumnCount()){
                return indicatorMoment.hasValue (index);
            }else{
                index -= indicatorMoment.getColumnCount();
                if (index < signalMoment.getColumnCount()){
                    return signalMoment.hasValue (index);
                }else{
                    throw new NoSuchElementException(
                            String.format("No element in StrategySeries at position %d", index));
                }
            }
        }
    }

    public int getColumnCount() {
        return baseMoment.getColumnCount()+indicatorMoment.getColumnCount()+signalMoment.getColumnCount();
    }

    public ColumnDefinition[] getColumnDefinition () {
        ColumnDefinition[] baseMomentColumnDefinition = baseMoment.getColumnDefinition ();
        ColumnDefinition[] indicatorMomentColumnDefinition = indicatorMoment.getColumnDefinition ();
        ColumnDefinition[] signalMomentColumnDefinition = signalMoment.getColumnDefinition ();
        ColumnDefinition[] columnDefinitions = new ColumnDefinition[baseMomentColumnDefinition.length + indicatorMomentColumnDefinition.length + signalMomentColumnDefinition.length];
        int overallIndex = 0;
        for (ColumnDefinition d : baseMomentColumnDefinition) {
            columnDefinitions[overallIndex++] = d;
        }
        for (ColumnDefinition d : indicatorMomentColumnDefinition) {
            columnDefinitions[overallIndex++] = d;
        }
        for (ColumnDefinition d : signalMomentColumnDefinition) {
            columnDefinitions[overallIndex++] = d;
        }
        return columnDefinitions;
        //return (ColumnDefinition[]) ArrayUtils.addAll (baseMoment.getColumnDefinition (),
          //                                             indicatorMoment.getColumnDefinition ());
    }

}
