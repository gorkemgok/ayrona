package com.ayronasystems.core.util;

import java.util.List;

/**
 * Created by gorkemgok on 21/05/16.
 */
public class NumberUtils {

    public static double[] toArray(List<Double> doubleList){
        double[] doubleArr = new double[doubleList.size ()];
        for ( int i = 0; i < doubleList.size (); i++ ) {
            doubleArr[i] = doubleList.get (i);
        }
        return doubleArr;
    }

}
