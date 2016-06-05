package com.ayronasystems.core.instant;

/**
 * Created by gorkemgok on 30/05/16.
 */
public class InstantBasicDoublePrecision {

    public static void main(String[] args){
        double d1 = 1.1;
        double d2 = 2.0;
        double d3 = 3.0;
        double t1 = d1 * d2 * d3;
        double t2_1 = d1 * d2;
        double t2 = t2_1 * d3;

        System.out.println (t1+", "+t2);
    }

}
