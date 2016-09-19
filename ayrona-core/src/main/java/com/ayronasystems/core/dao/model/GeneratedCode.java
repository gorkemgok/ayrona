package com.ayronasystems.core.dao.model;

import org.mongodb.morphia.annotations.Embedded;

/**
 * Created by gorkemgok on 10/09/16.
 */
@Embedded
public class GeneratedCode {

    private String code;

    private double fitness;

    private int generationCount;

    public String getCode () {
        return code;
    }

    public void setCode (String code) {
        this.code = code;
    }

    public double getFitness () {
        return fitness;
    }

    public void setFitness (double fitness) {
        this.fitness = fitness;
    }

    public int getGenerationCount () {
        return generationCount;
    }

    public void setGenerationCount (int generationCount) {
        this.generationCount = generationCount;
    }
}
