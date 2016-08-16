package com.ayronasystems.core.backtest;

import com.ayronasystems.core.dao.mysql.Tick4JDao;
import com.ayronasystems.core.dao.mysql.Tick4JDaoImpl;
import com.ayronasystems.core.dao.mysql.model.StrategyModel;
import com.ayronasystems.core.dao.mysql.model.StrategySessionModel;
import com.ayronasystems.core.dao.mysql.model.SummaryModel;
import com.ayronasystems.core.definition.BackTestType;
import com.ayronasystems.core.strategy.Position;
import com.ayronasystems.core.strategy.StrategyOptions;

import java.util.*;

/**
 * Created by gorkemgok on 19/01/16.
 */
public class BackTestSummary {

    public static class Key {

        public static final Key TRAINING_PERFORMANCE_SUMMARY = new Key (PerformanceSummary.class,
                                                                        BackTestType.TRAINING);

        public static final Key TEST_PERFORMANCE_SUMMARY = new Key (PerformanceSummary.class, BackTestType.TEST);

        public static final Key VALIDATION_PERFORMANCE_SUMMARY = new Key (PerformanceSummary.class,
                                                                          BackTestType.VALIDATION);

        public static final Key TRAINING_EQUITY_SUMMARY = new Key (EquitySummary.class, BackTestType.TRAINING);

        public static final Key TEST_EQUITY_SUMMARY = new Key (EquitySummary.class, BackTestType.TEST);

        public static final Key VALIDATION_EQUITY_SUMMARY = new Key (EquitySummary.class,
                                                                     BackTestType.VALIDATION);
        public static final Key[] KEYS = {TRAINING_EQUITY_SUMMARY, TEST_EQUITY_SUMMARY, VALIDATION_EQUITY_SUMMARY, TRAINING_PERFORMANCE_SUMMARY, TEST_PERFORMANCE_SUMMARY, VALIDATION_PERFORMANCE_SUMMARY};

        private Class<? extends Summary> classOfSummary;

        private BackTestType backTestType;

        public Key (Class<? extends Summary> classOfSummary, BackTestType backTestType) {
            this.classOfSummary = classOfSummary;
            this.backTestType = backTestType;
        }

        public Class<? extends Summary> getClassOfSummary () {
            return classOfSummary;
        }

        public BackTestType getBackTestType () {
            return backTestType;
        }

        @Override
        public boolean equals (Object obj) {
            if ( obj instanceof Key ) {
                Key key = (Key)obj;
                if (key.getClassOfSummary ().equals (this.getClassOfSummary ()) && key.getBackTestType ().equals (this.getBackTestType ())){
                    return true;
                }
            }
            return false;
        }

        @Override
        public int hashCode () {
            return backTestType.hashCode ();
        }
    }

    private Tick4JDao dao = Tick4JDaoImpl.getDao ();

    private String function;

    private StrategyOptions strategyOptions;

    private Map<Key, Summary> summaryMap = new HashMap<Key, Summary> ();

    private List<Position> positions = null;

    public BackTestSummary (String function, StrategyOptions strategyOptions) {
        this.function = function;
        this.strategyOptions = strategyOptions;
    }

    public boolean addSummary (Key key, Summary summary) {
        if ( !summaryMap.containsKey (key) ) {
            summaryMap.put (key, summary);
            return true;
        }
        return false;
    }

    public Summary getSummary (Key key) {
        return summaryMap.get (key);
    }

    public void persist (StrategySessionModel strategySessionModel, Key fitnessKey) {
        StrategyModel strategyModel = new StrategyModel (function, getSummary (fitnessKey).getResultAsDouble (
                strategyOptions.getFitnessMetric ()), strategySessionModel);
        persist (strategyModel);
    }

    public void persist (StrategyModel strategyModel) {
        Set<SummaryModel> summaries = new HashSet<SummaryModel> ();
        for ( Key key : Key.KEYS ) {
            Summary summary = getSummary (key);
            if ( summary != null ) {
                SummaryModel trainingSummaryModel = SummaryModel.valueOf (summary);
                trainingSummaryModel.setBackTestType (key.getBackTestType ());
                summaries.add (trainingSummaryModel);
            }
        }

        strategyModel.setSummaries (summaries);
        dao.save (strategyModel);
    }

    public List<Position> getPositions () {
        return positions;
    }

    public void setPositions (List<Position> positions) {
        this.positions = positions;
    }
}
