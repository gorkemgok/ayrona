package com.ayronasystems.core.timeseries.moment;


import com.ayronasystems.core.timeseries.type.Indicator;

import java.util.*;

/**
 * Created by gorkemgok on 28/06/15.
 */
public class IndicatorMoment extends AbstractMoment {

    private List<Indicator> indicators = new ArrayList<Indicator>();

    public IndicatorMoment(Date timestamp) {
        super(timestamp);
    }

    public IndicatorMoment(Date timestamp, Indicator initialIndicator) {
        super(timestamp);
        this.addIndicator(initialIndicator);
    }

    public Indicator addIndicator(Indicator indicator){
        Indicator ind;
        if ( (ind = this.hasIndicator (indicator)) != null ) {
            return ind;
        }else {
            indicators.add (indicator);
            return indicator;
        }
    }

    public Indicator hasIndicator(Indicator indicator){
        for ( Indicator ind : indicators ) {
            if (ind.isSame (indicator)){
                return ind;
            }
        }
        return null;
    }

    public Indicator find(String name, double[] params){
        for (Indicator indicator : indicators){
            if (indicator.getName().equals(name) && Arrays.equals(indicator.getParameters(),params)){
                return indicator;
            }
        }
        return null;
    }

    public double get(ColumnDefinition columnDefinition) {
        return getIndicator(columnDefinition).getValue();
    }

    private Indicator getIndicator (ColumnDefinition columnDefinition){
        for (Indicator indicator : indicators){
            if (columnDefinition.getColumnType() == ColumnType.INDICATORVALUE
                    && columnDefinition.getColumnName().equals(indicator.getName())
                    && Arrays.equals(columnDefinition.getParams(), indicator.getParameters())){
                return indicator;
            }
        }
        throw new NoSuchElementException(String.format("There in no calculated indicator: %s ", columnDefinition.getColumnName()));

    }

    public void set(ColumnDefinition columnDefinition, double value) {
        for (Indicator indicator : indicators){
            if (columnDefinition.getColumnType() == ColumnType.INDICATORVALUE
                    && columnDefinition.getColumnName().equals(indicator.getName())
                    && Arrays.equals(columnDefinition.getParams(), indicator.getParameters())){
               indicator.setValue(value);
            }
        }
        throw new NoSuchElementException(String.format("There in no calculated indicator: %s ", columnDefinition.getColumnName()));

    }

    public int getIndex(ColumnDefinition columnDefinition) {
        int index = 0;
        for (Indicator indicator : indicators) {
            if (columnDefinition.getColumnType() == ColumnType.INDICATORVALUE
                    && columnDefinition.getColumnName().equals(indicator.getName())
                    && Arrays.equals(columnDefinition.getParams(), indicator.getParameters())) {
                return index;
            }
            index++;
        }
        throw new NoSuchElementException(String.format("There in no calculated indicator: %s ", columnDefinition.getColumnName()));

    }

    public double get(int index) {
        if (index < indicators.size ()) {
            return indicators.get (index)
                             .getValue ();
        }else{
            throw new NoSuchElementException (String.format("There in no calculated indicator at index: %d ", index));
        }
    }

    public void set(int index, double value) {
        if (index < indicators.size ()) {
            indicators.get(index).setValue(value);
        }else{
            throw new NoSuchElementException (String.format("There in no calculated indicator at index: %d ", index));
        }
    }

    public boolean hasNotCalculatedValue() {
        for (Indicator indicator : indicators){
            if (!indicator.isCalculated()){
                return true;
            }
        }
        return false;
    }

    public int getColumnCount() {
        return indicators.size();
    }

    public ColumnDefinition[] getColumnDefinition () {
        ColumnDefinition[] columnDefinitions = new ColumnDefinition[indicators.size ()];
        int i = 0;
        for (Indicator indicator : indicators){
            columnDefinitions[i++] = new ColumnDefinition (ColumnType.INDICATORVALUE, indicator.getName (), indicator.getParameters());
        }
        return columnDefinitions;
    }

    @Override
    public boolean hasValue(ColumnDefinition columnDefinition) {
        try {
            return getIndicator (columnDefinition).isCalculated ();
        }catch ( NoSuchElementException ex ){
            throw ex;
        }
    }


    @Override
    public boolean hasValue(int index) {
        if (index < indicators.size ()) {
            return indicators.get (index).isCalculated ();
        }else{
            throw new NoSuchElementException (String.format("There in no calculated indicator at index: %d ", index));
        }
    }

}
