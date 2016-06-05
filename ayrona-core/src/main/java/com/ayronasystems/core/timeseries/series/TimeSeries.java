package com.ayronasystems.core.timeseries.series;

import com.ayronasystems.core.timeseries.moment.Moment;

import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 21/06/15.
 */
public interface TimeSeries<M extends Moment> extends Iterable<M>{

    M addMomentSafe (M m);

    void addMoment (M m);

    M getMoment (int index);

    M getMoment (Date timestamp);

    int getMomentCount ();

    int getColumnCount ();

    Date getBeginningDate ();

    Date getEndingDate ();

    double[] toArray (int columnIndex);

    double[][] toArrays (int... columnIndexes);

    double[][] toArrays (int count, List<Integer> columnIndexes);

}
