package com.ayronasystems.core.definition;

/**
 * Created by gorkemgok on 14/05/16.
 */
public enum Direction{
    LONG, SHORT, AMBIGUOUS;

    public Direction inverse(){
        switch ( this ){
            case LONG:
                return SHORT;
            case SHORT:
                return LONG;
            default:
                return AMBIGUOUS;
        }
    }
}
