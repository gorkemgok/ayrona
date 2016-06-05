package com.ayronasystems.core.exception;

/**
 * Created by gorkemgok on 19/03/16.
 */
public class FunctionNotFoundException extends RuntimeException {

    public FunctionNotFoundException (String message) {
        super (message+" core not found");
    }
}
