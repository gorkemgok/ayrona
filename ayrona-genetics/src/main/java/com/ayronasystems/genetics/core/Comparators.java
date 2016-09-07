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
            return o1.getFitnessValue () > o2.getFitnessValue () ? 1 : o1.getFitnessValue () < o2.getFitnessValue () ? -1 : 0;
        }
    };

    public static final Comparator<Chromosome> SMALLER_IS_FITTER = new Comparator<Chromosome> () {
        public int compare (Chromosome o1, Chromosome o2) {
            return o1.getFitnessValue () > o2.getFitnessValue () ? -1 : o1.getFitnessValue () < o2.getFitnessValue () ? 1 : 0;
        }
    };
}
