package com.ayronasystems.genetics.core;

/**
 * Created by gorkemg on 28.03.2016.
 */
public interface MutationMethod<T> {

    T apply(Chromosome<T> chromosome, double probability);
}
