package com.ayronasystems.core.timeseries.series;

import com.ayronasystems.core.exception.ElementIsOverwrittenException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by gorkemgok on 14/01/16.
 */
public class SeriesList<T> implements List<T> {

    private int capacity;

    private int size;

    private int relativeZeroPoint;

    private int zeroPoint;

    private int removedCount;

    private T[] array;

    public SeriesList (int capacity) {
        this.capacity = capacity;
        array = (T[])new Object[capacity];
    }

    public int size () {
        return size;
    }

    public boolean isEmpty () {
        return size == 0;
    }

    public boolean contains (Object o) {
        for (T element : array){
            if (o.equals (element)){
                return true;
            }
        }
        return false;
    }

    public Iterator<T> iterator () {
        return new SeriesListIterator<T> (this);
    }

    public Object[] toArray () {
        Object[] newArray = new Object[capacity];
        int i = 0;
        for (T element : array){
            newArray[i] = element;
            i++;
        }
        return newArray;
    }

    public <T1> T1[] toArray (T1[] a) {
        int i = 0;
        for (T element : array){
            a[i] = (T1)element;
            i++;
        }
        return a;
    }

    public boolean add (T t) {
        if ( size < capacity ){
            array[size] = t;
        }else{
            array[zeroPoint] = t;
            zeroPoint ++;
            removedCount ++;
            if (zeroPoint == capacity ){
                zeroPoint = 0;
                relativeZeroPoint = removedCount;
            }
        }
        size++;
        return true;
    }

    public T get (int index) {
        if ( index < removedCount ){
            throw new ElementIsOverwrittenException (removedCount);
        }else{
            int realIndex = (relativeZeroPoint + index);
            if (realIndex < capacity) {
                return array[realIndex];
            }else{
                return array[realIndex - capacity];
            }
        }
    }

    public boolean containsAll (Collection<?> c) {
        Iterator iterator = c.iterator ();
        while ( iterator.hasNext () ){
            Object object = iterator.next ();
            if (!contains (object)){
                return false;
            }
        }
        return true;
    }

    public boolean addAll (Collection<? extends T> c) {
        Iterator iterator = c.iterator ();
        while ( iterator.hasNext () ){
            Object object = iterator.next ();
            add ((T)object);
        }
        return true;
    }

    public void clear () {
        array = (T[])new Object[capacity];
        size = 0;
        zeroPoint = 0;
        removedCount = 0;
        relativeZeroPoint = 0;
    }

    public T set (int index, T element) {
        if (index > size - 1){
            throw new ArrayIndexOutOfBoundsException (index);
        }
        if ( index < removedCount ){
            throw new ElementIsOverwrittenException (removedCount);
        }else{
            int realIndex = index - removedCount;
            int modIndex = (zeroPoint +realIndex) % capacity;
            T prevElement =  array[ modIndex ];
            array[ modIndex ] = element;
            return prevElement;
        }
    }

    public int indexOf (Object o) {
        int i = 0;
        for (T element : array){
            if (o.equals (element)){
                //TODO:implement indexOf
                return 0;
            }
            i++;
        }
        return -1;
    }

    public int lastIndexOf (Object o) {
        //TODO:implement lastIndexOf
        return 0;
    }

    public ListIterator<T> listIterator () {
        //TODO:implement listIterator
        return null;
    }

    public ListIterator<T> listIterator (int index) {
        //TODO:implement listIterator(index)
        return null;
    }

    public List<T> subList (int fromIndex, int toIndex) {
        List<T> newList = new SeriesList<T> (toIndex - fromIndex + 1);
        for (int i = fromIndex; i <= toIndex; i++){
            newList.add (get (i));
        }
        return newList;
    }

    public boolean retainAll (Collection<?> c) {
        throw new NotImplementedException ();
    }

    public boolean addAll (int index, Collection<? extends T> c) {
        throw new NotImplementedException ();
    }

    public void add (int index, T element) {
        throw new NotImplementedException ();
    }

    public T remove (int index) {
        throw new NotImplementedException ();
    }

    public boolean remove (Object o) {
        throw new NotImplementedException ();
    }

    public boolean removeAll (Collection<?> c) {
        throw new NotImplementedException ();
    }
}
