package com.ayronasystems.core.algo;

import com.ayronasystems.core.exception.PrerequisiteException;

/**
 * Created by gorkemgok on 05/02/15.
 */
public interface Function {

    FunctionDefinition getDefinition ();

    /**
     * Returns the number of minimum input to calculate 1 result.
     * For example to calculate needed input count for AVERAGE (formula depth = 1) of 10 period is 10. AVERAGEofAVERAGE (formula depth = 2) of 10 period is 19.
     * needed input count = period * formula depth - 1
     * @param params Function parameters
     * @return Returns the number of minimum input to calculate 1 result.
     */
    int getNeededInputCount (double... params);

    /**
     * Output count depends on needed input count and input data size.
     * For AVERAGE core it is minimum input count - needed input count + 1.
     * For AVERAGEofAVERAGE it is minimum input count - needed input count * 2 + 1.
     * @param inputs Market data
     * @param params Function parameter
     * @return Returns output of the core with given input and parameters.
     */
    int getOutputCount (FIOExchange inputs, double... params);

    FIOExchange calculate (FIOExchange inputs, double... params);

    void checkPrerequisites (FIOExchange inputs, double... params) throws PrerequisiteException;
}
