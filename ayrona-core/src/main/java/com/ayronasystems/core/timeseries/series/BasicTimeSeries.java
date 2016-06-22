package com.ayronasystems.core.timeseries.series;

import com.ayronasystems.core.timeseries.moment.*;
import com.ayronasystems.core.timeseries.type.Indicator;
import com.google.common.base.Joiner;

import java.util.*;

/**
 * Created by gorkemgok on 21/06/15.
 */
public class BasicTimeSeries<M extends Moment> implements TimeSeries<M>, IterableSeries<M>{

    public Iterator<M> iterator () {
        return new SeriesIterator<BasicTimeSeries<M>,M> (this, 0);
    }

    public enum ListType{
        DYNAMIC_SIZE,
        FIXED_SIZE
    }

    private List<M> moments;

    private Class<M> classOfMoment;

    protected BasicTimeSeries(Class<M> classOfMoment) {
        this.classOfMoment = classOfMoment;
        moments = new ArrayList<M>();
    }

    protected BasicTimeSeries(Class<M> classOfMoment, ListType listType, int initialListSize) {
        this.classOfMoment = classOfMoment;
        if (listType == ListType.DYNAMIC_SIZE){
            if (initialListSize > 0) {
                moments = new ArrayList<M> (initialListSize);
            }else{
                moments = new ArrayList<M> ();
            }
        }else if (listType == ListType.FIXED_SIZE){
            moments = new SeriesList<M> (initialListSize);
        }else{
            moments = new ArrayList<M>();
        }
    }

    public static <Mm extends Moment> BasicTimeSeries getInstance(Class<Mm> classOfMoment){
        return new BasicTimeSeries<Mm>(classOfMoment);
    }

    public static <Mm extends Moment> BasicTimeSeries getDynamicSizeInstance(Class<Mm> classOfMoment){
        return new BasicTimeSeries<Mm>(classOfMoment, ListType.DYNAMIC_SIZE, 0);
    }

    public static <Mm extends Moment> BasicTimeSeries getDynamicSizeInstance(Class<Mm> classOfMoment, int initialSize){
        return new BasicTimeSeries<Mm>(classOfMoment, ListType.DYNAMIC_SIZE, initialSize);
    }

    public static <Mm extends Moment> BasicTimeSeries getFixedSizeInstance(Class<Mm> classOfMoment, int initialSize){
        return new BasicTimeSeries<Mm>(classOfMoment, ListType.DYNAMIC_SIZE, initialSize);
    }

    public Date getBeginningDate(){
        return moments.get (0).getDate ();
    }

    public Date getEndingDate(){
        return moments.get (moments.size () - 1).getDate ();
    }

    public double[] toArray (int columnIndex) {
        double[] result = new double[moments.size ()];
        int i = 0;
        for (Moment moment : moments){
            result[i] = moment.get (columnIndex);
            i++;
        }
        return result;
    }

    public double[][] toArrays (int... columnIndexes) {
        double[][] result = new double[columnIndexes.length][moments.size ()];
        int i = 0;
        for (Moment moment : moments){
            for ( int j = 0; j < columnIndexes.length; j++ ) {
                result[j][i] = moment.get (columnIndexes[j]);
            }
            i++;
        }
        return result;
    }

    public double[][] toArrays (int count, List<Integer> columnIndexes) {
        double[][] result = new double[columnIndexes.size ()][count];
        int i = 0;
        int offset = moments.size () - count;
        for (int k = offset; k < moments.size (); k++){
            Moment moment = moments.get (k);
            for ( int j = 0; j < columnIndexes.size (); j++ ) {
                result[j][i] = moment.get (columnIndexes.get (j));
            }
            i++;
        }
        return result;
    }

    public void addMoment (M m) {
        moments.add (m);
    }

    public M addMomentSafe (M m) {
        for ( M moment : moments ) {
            if ( m.getDate ()
                  .equals (moment.getDate ()) ) {
                return moment;
            }
        }
        moments.add (m);
        return m;
    }

    public M getMoment (int index) {
        return moments.get(index);
    }

    public M getMoment(Date timestamp) {
        for (M moment : moments) {
            if (moment.getDate()
                      .equals(timestamp)) {
                return moment;
            }
        }
        throw new NoSuchElementException(String.format("No complete moment with timestamp: %s",timestamp.toString()));
    }

    public SeriesIterator<BasicTimeSeries<M>, M> begin() {
        return new SeriesIterator<BasicTimeSeries<M>, M>(this);
    }

    public SeriesIterator<BasicTimeSeries<M>, M> beginFrom (int index) {
        return new SeriesIterator<BasicTimeSeries<M>, M>(this, index);
    }

    public int size () {
        return moments.size();
    }

    public BasicTimeSeries<M> unwrapSeries() {
        return this;
    }

    public int getColumnCount() {
        if (Bar.class.equals(classOfMoment)) {
            return Bar.COLUMN_COUNT;
        } else if (Tick.class.equals(classOfMoment)) {
            return Tick.COLUMN_COUNT;
        } else if (Indicator.class.equals(classOfMoment)) {
            return Indicator.COLUMN_COUNT;
        } else if (SignalMoment.class.equals(classOfMoment)) {
            return SignalMoment.COLUMN_COUNT;
        } else {
            throw new RuntimeException("Ohh my god.A class other than Moment in TimeSeries!");
        }
    }

    @Override
    public String toString () {
        return convertToString (moments);
    }

    public static String convertToString (List<? extends Moment> moments) {
        StringBuilder stringBuilder = new StringBuilder ();
        boolean isFirstRow = true;
        int columnCount = 0;
        for ( Moment moment : moments ) {
            ColumnDefinition[] columnDefinitions = moment.getColumnDefinition ();
            if ( isFirstRow ) {
                stringBuilder.append (String.format ("%30s", "DATE\t"));
                columnCount = moment.getColumnCount ();
                for (ColumnDefinition columnDefinition : columnDefinitions ) {
                    StringBuilder paramsStringBuilder = new StringBuilder()
                                                            .append (columnDefinition.getColumnName ());

                    if (columnDefinition.getParams() != null){

                        Object[] objects = new Object[columnDefinition.getParams ().length];
                        for ( int i = 0; i < columnDefinition.getParams ().length; i++ ) {
                            objects[i] = columnDefinition.getParams ()[i];
                        }
                        paramsStringBuilder.append("(")
                            .append (Joiner.on (", ")
                                           .join (objects))
                            .append(")");
                    }
                    stringBuilder.append (String.format ("%14s", paramsStringBuilder.toString()))
                                 .append("\t");
                }
                stringBuilder.append("\n");
                isFirstRow = false;
            }
            stringBuilder.append (moment.toString ());
            stringBuilder.append ("\n");
        }
        return stringBuilder.toString ();
    }

}
