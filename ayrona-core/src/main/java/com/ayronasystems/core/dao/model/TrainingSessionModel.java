package com.ayronasystems.core.dao.model;

import com.ayronasystems.core.definition.Period;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 09/09/16.
 */
@Entity
public class TrainingSessionModel extends BaseModel{

    public enum State{
        WAITING,
        RUNNING,
        CANCELED,
        STOPPED
    }

    private String name;

    private String symbol;

    private Period period;

    private Date startDate;

    private Date endDate;

    private int populationSize;

    private double mutationProbability;

    private int eliteCount;

    private int threadCount;

    private State state;

    private List<GeneratedCode> generatedCodeList;

    private String scoreEquation;

    public String getScoreEquation () {
        return scoreEquation;
    }

    public void setScoreEquation (String scoreEquation) {
        this.scoreEquation = scoreEquation;
    }

    public List<GeneratedCode> getGeneratedCodeList () {
        return generatedCodeList;
    }

    public void setGeneratedCodeList (List<GeneratedCode> generatedCodeList) {
        this.generatedCodeList = generatedCodeList;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public State getState () {
        return state;
    }

    public void setState (State state) {
        this.state = state;
    }

    public String getSymbol () {
        return symbol;
    }

    public void setSymbol (String symbol) {
        this.symbol = symbol;
    }

    public Period getPeriod () {
        return period;
    }

    public void setPeriod (Period period) {
        this.period = period;
    }

    public Date getStartDate () {
        return startDate;
    }

    public void setStartDate (Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate () {
        return endDate;
    }

    public void setEndDate (Date endDate) {
        this.endDate = endDate;
    }

    public int getPopulationSize () {
        return populationSize;
    }

    public void setPopulationSize (int populationSize) {
        this.populationSize = populationSize;
    }

    public double getMutationProbability () {
        return mutationProbability;
    }

    public void setMutationProbability (double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    public int getEliteCount () {
        return eliteCount;
    }

    public void setEliteCount (int eliteCount) {
        this.eliteCount = eliteCount;
    }

    public int getThreadCount () {
        return threadCount;
    }

    public void setThreadCount (int threadCount) {
        this.threadCount = threadCount;
    }

    @Override
    public boolean equals (Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass () != o.getClass () ) {
            return false;
        }

        TrainingSessionModel that = (TrainingSessionModel) o;

        if ( populationSize != that.populationSize ) {
            return false;
        }
        if ( Double.compare (that.mutationProbability, mutationProbability) != 0 ) {
            return false;
        }
        if ( eliteCount != that.eliteCount ) {
            return false;
        }
        if ( threadCount != that.threadCount ) {
            return false;
        }
        if ( !symbol.equals (that.symbol) ) {
            return false;
        }
        if ( period != that.period ) {
            return false;
        }
        if ( !startDate.equals (that.startDate) ) {
            return false;
        }
        if ( !endDate.equals (that.endDate) ) {
            return false;
        }
        return state == that.state;

    }

    @Override
    public int hashCode () {
        int result;
        long temp;
        result = symbol.hashCode ();
        result = 31 * result + period.hashCode ();
        result = 31 * result + startDate.hashCode ();
        result = 31 * result + endDate.hashCode ();
        result = 31 * result + populationSize;
        temp = Double.doubleToLongBits (mutationProbability);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + eliteCount;
        result = 31 * result + threadCount;
        result = 31 * result + state.hashCode ();
        return result;
    }
}
