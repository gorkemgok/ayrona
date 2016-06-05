package com.ayronasystems.core.algo;

import com.ayronasystems.core.exception.IllegalInputException;
import com.ayronasystems.core.exception.IllegalParameterException;
import com.ayronasystems.core.exception.PrerequisiteException;

/**
 * Created by gorkemgok on 17/03/16.
 */
public abstract class AbstractFunction implements Function{

    private FunctionDefinition definition;

    public AbstractFunction () {
        Fn fn = this.getClass ().getAnnotation (Fn.class);
        String name = fn.name ();
        int inputCount = fn.inputCount ();
        int paramCount = fn.paramCount ();
        int depth = fn.depth ();
        int begOffset = fn.begOffset ();
        boolean sameSizeInput = fn.sameSizeInput ();
        FunctionDefinition definition = new FunctionDefinition (name, inputCount, paramCount, depth, begOffset, sameSizeInput);
        this.definition = definition;
    }

    public FunctionDefinition getDefinition () {
        return definition;
    }

    public void checkPrerequisites (FIOExchange inputs, double... params) throws PrerequisiteException {
        if (inputs.getDataCount () < definition.getInputCount ()){
            throw new IllegalInputException (definition.getName ()+" core needs "+inputs.getDataCount ()+" inputs");
        }

        if (params.length < definition.getParamCount ()){
            throw  new IllegalParameterException (definition.getName ()+" core need "+params.length+" parameters");
        }

        if (definition.isSameSizeInput ()) {
            for ( int i = 1; i < inputs.getDataCount (); i++ ) {
                if ( inputs.getDataSize (0) != inputs.getDataSize (i) ) {
                    throw new IllegalInputException (definition.getName () + " core need inputs to be in same size");
                }
            }
        }

        if (inputs.getDataSize (0) < getNeededInputCount (params)){
            throw new IllegalInputException (definition.getName ()+" core inputs has less than needed element");
        }
    }
}
