package com.ayronasystems.core.timeseries.series;

import java.util.Iterator;
import java.util.List;

/**
 * Created by gorkemgok on 15/01/16.
 */
public class SeriesListIterator<T> implements Iterator<T>{

    private int currentIndex;

    private List<T> list;

    public SeriesListIterator (List<T> list) {
        this.list = list;
    }

    public boolean hasNext () {
        if ( currentIndex < list.size () ) {
            return true;
        }
        return false;
    }

    public T next () {
        return list.get (currentIndex);
    }

    public void remove () {
        throw new UnsupportedOperationException ();
    }
}
