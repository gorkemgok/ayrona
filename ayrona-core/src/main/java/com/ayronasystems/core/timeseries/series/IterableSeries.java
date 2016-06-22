package com.ayronasystems.core.timeseries.series;

import com.ayronasystems.core.timeseries.moment.Moment;

/**
 * Created by gorkemgok on 28/06/15.
 */
public interface IterableSeries<M extends Moment> {

    SeriesIterator<? extends IterableSeries<M>,M> begin ();

    SeriesIterator<? extends IterableSeries<M>,M> beginFrom (int index);

    M getMoment (int index);

    int size ();

    TimeSeries unwrapSeries ();

}
