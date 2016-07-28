package com.ayronasystems.core.util;

import java.util.Random;

/**
 * Created by gorkemgok on 17/07/16.
 */
public class RandomUtils {

    public static double getDouble(double start, double end, double step){
        int count = new Double(end - start / step).intValue ();
        int random = new Random().nextInt (count);
        return start + (step * random);
    }
}
