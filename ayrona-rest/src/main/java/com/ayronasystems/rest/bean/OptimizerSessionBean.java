package com.ayronasystems.rest.bean;

import com.ayronasystems.core.dao.model.GeneratedCode;
import com.ayronasystems.core.dao.model.OptimizerSessionModel;
import com.ayronasystems.core.dao.model.TrainingSessionModel;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.util.DateUtils;

import java.util.List;

/**
 * Created by gorkemgok on 17/09/16.
 */
public class OptimizerSessionBean {

    private String id;

    private String name;

    private String code;

    private String symbol;

    private String period;

    private String startDate;

    private String endDate;

    private int populationSize;

    private double mutationProbability;

    private int eliteCount;

    private int threadCount;

    private String state;

    private String createDate;

    private String scoreEquation;

    private List<GeneratedCode> generatedCodeList;

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getCode () {
        return code;
    }

    public void setCode (String code) {
        this.code = code;
    }

    public String getSymbol () {
        return symbol;
    }

    public void setSymbol (String symbol) {
        this.symbol = symbol;
    }

    public String getPeriod () {
        return period;
    }

    public void setPeriod (String period) {
        this.period = period;
    }

    public String getStartDate () {
        return startDate;
    }

    public void setStartDate (String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate () {
        return endDate;
    }

    public void setEndDate (String endDate) {
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

    public String getState () {
        return state;
    }

    public void setState (String state) {
        this.state = state;
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

    public String getCreateDate () {
        return createDate;
    }

    public void setCreateDate (String createDate) {
        this.createDate = createDate;
    }

    public String getScoreEquation () {
        return scoreEquation;
    }

    public void setScoreEquation (String scoreEquation) {
        this.scoreEquation = scoreEquation;
    }

    public static OptimizerSessionBean valueOf(OptimizerSessionModel model){
        OptimizerSessionBean bean = new OptimizerSessionBean ();
        bean.setId (model.getId ());
        bean.setName (model.getName ());
        bean.setSymbol (model.getSymbol ());
        bean.setPeriod (model.getPeriod ().toString ());
        bean.setCode ( model.getCode () );
        bean.setPopulationSize (model.getPopulationSize ());
        bean.setThreadCount ( model.getThreadCount () );
        bean.setEliteCount (model.getEliteCount ());
        bean.setMutationProbability (model.getMutationProbability ());
        bean.setStartDate ( DateUtils.toISO (model.getStartDate ()) );
        bean.setEndDate ( DateUtils.toISO (model.getEndDate ()) );
        bean.setState (model.getState ().toString ());
        bean.setGeneratedCodeList (model.getGeneratedCodeList ());
        bean.setCreateDate ( DateUtils.toISO (model.getCreateDate ()));
        bean.setScoreEquation (model.getScoreEquation ());
        return bean;
    }

    public OptimizerSessionModel to(){
        OptimizerSessionModel model = new OptimizerSessionModel ();
        model.setId (this.getId ());
        model.setName (this.getName ());
        model.setSymbol (this.getSymbol ());
        model.setPeriod (Period.valueOf (this.getPeriod ()));
        model.setCode (this.getCode ());
        model.setPopulationSize (this.getPopulationSize ());
        model.setThreadCount (this.getThreadCount ());
        model.setEliteCount (this.getEliteCount ());
        model.setMutationProbability (this.getMutationProbability ());
        model.setStartDate (DateUtils.convertFromISO (this.startDate));
        model.setEndDate (DateUtils.convertFromISO (this.endDate));
        model.setGeneratedCodeList (this.getGeneratedCodeList ());
        model.setState (TrainingSessionModel.State.valueOf (this.state));
        model.setScoreEquation (this.scoreEquation);
        return model;
    }
}
