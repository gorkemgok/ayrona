package com.ayronasystems.genetics.core;

import java.util.Comparator;

/**
 * Created by gorkemgok on 04/04/16.
 */
public class Comparators {

    private Comparators () {
    }

    public static final Comparator<Chromosome> BIGGER_IS_FITTER = new Comparator<Chromosome> () {
        public int compare (Chromosome o1, Chromosome o2) {
            double v1 = o1.getFitnessValue ();
            double v2 = o2.getFitnessValue ();
            if (Double.isNaN (v1) && Double.isNaN (v2)){
                return 0;
            }
            if (Double.isNaN (v1)){
                return -1;
            }
            if (Double.isNaN (v2)){
                return 1;
            }
            return  v1 > v2 ? 1 : v1 < v2 ? -1 : 0;
        }
    };

    public static final Comparator<Chromosome> SMALLER_IS_FITTER = new Comparator<Chromosome> () {
        public int compare (Chromosome o1, Chromosome o2) {
            double v1 = o1.getFitnessValue ();
            double v2 = o2.getFitnessValue ();
            if (Double.isNaN (v1) && Double.isNaN (v2)){
                return 0;
            }
            if (Double.isNaN (v1)){
                return 1;
            }
            if (Double.isNaN (v2)){
                return -1;
            }
            return  v1 > v2 ? -1 : v1 < v2 ? 1 : 0;
        }
    };
}
