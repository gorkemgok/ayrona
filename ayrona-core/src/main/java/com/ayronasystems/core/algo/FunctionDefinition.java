package com.ayronasystems.core.algo;

/**
 * Created by gorkemgok on 19/03/16.
 */
public class FunctionDefinition {

    private String name;

    private int inputCount;

    private int paramCount;

    private int depth;

    private int begOffset;

    private boolean sameSizeInput;

    public FunctionDefinition (String name, int inputCount, int paramCount, int depth, int begOffset, boolean sameSizeInput) {
        this.name = name;
        this.inputCount = inputCount;
        this.paramCount = paramCount;
        this.depth = depth;
        this.begOffset = begOffset;
        this.sameSizeInput = sameSizeInput;
    }

    public boolean isSameSizeInput () {
        return sameSizeInput;
    }

    public String getName () {
        return name;
    }

    public int getInputCount () {
        return inputCount;
    }

    public int getParamCount () {
        return paramCount;
    }

    public int getDepth () {
        return depth;
    }

    public int getBegOffset () {
        return begOffset;
    }
}
