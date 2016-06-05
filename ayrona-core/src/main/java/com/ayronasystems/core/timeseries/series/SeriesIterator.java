package com.ayronasystems.core.timeseries.series;


import com.ayronasystems.core.timeseries.moment.Moment;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by gorkemgok on 28/06/15.
 */
public class SeriesIterator<S extends IterableSeries<M>, M extends Moment> implements Iterator<M> {

    private int currentIndex = -1;

    private S iterableSeries;

    public SeriesIterator(S iterableSeries) {
        this.iterableSeries = iterableSeries;
    }

    public SeriesIterator(S iterableSeries, int currentIndex) {
        this.currentIndex = currentIndex;
        this.iterableSeries = iterableSeries;
    }

    public void reset() {
            currentIndex = -1;
    }

    public void resetTo( int index ){
        currentIndex = index;
    }

    public M next() {
        currentIndex++;
        return getMoment();
    }

    public void remove () {
        throw new UnsupportedOperationException();
    }

    public M getMoment() {
        if (currentIndex < iterableSeries.getMomentCount()) {
            try {
                return iterableSeries.getMoment(currentIndex);
            }catch(ArrayIndexOutOfBoundsException ex){
                System.out.print(currentIndex);
            }
        }
        throw new NoSuchElementException ("Series Iterator has no more element");
    }

    public M getMomentOffset(int offset) {
        if (currentIndex + offset > -1  &&  (currentIndex + offset) < iterableSeries.getMomentCount()) {
            try {
                return iterableSeries.getMoment(currentIndex + offset);
            }catch(ArrayIndexOutOfBoundsException ex){
                System.out.print(currentIndex + offset);
            }
        }
        throw new NoSuchElementException("Series Iterator has no element at that offset");
    }

    public boolean hasNext(){
        if (currentIndex < iterableSeries.getMomentCount()-1) {
            return true;
        }
        return false;
    }

    public void seek(int index){
        currentIndex = index;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public S getSeries(){
        return iterableSeries;
    }
}
