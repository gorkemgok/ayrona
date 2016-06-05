package com.ayronasystems.core.exception;

/**
 * Created by gorkemgok on 14/01/16.
 */
public class ElementIsOverwrittenException extends RuntimeException{

    private int firstAvailableElementIndex;

    public ElementIsOverwrittenException (int firstAvailableElementIndex) {
        this.firstAvailableElementIndex = firstAvailableElementIndex;
    }

    public int getFirstAvailableElementIndex () {
        return firstAvailableElementIndex;
    }
}
