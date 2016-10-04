package com.ayronasystems.genetics.optimizer;

import com.ayronasystems.core.algo.Algo;
import com.ayronasystems.core.algo.OptimizerDef;
import com.ayronasystems.core.dao.model.OptimizerSessionModel;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.definition.Symbols;
import com.ayronasystems.core.service.StandaloneMarketDataService;
import com.ayronasystems.genetics.core.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 22/07/16.
 */
public class Optimizer extends Thread{

    private String                    id;
    private String                    code;
    private Symbol                    symbol;
    private Period                    period;
    private Date                      startDate;
    private Date                      endDate;
    private int                       populationSize;
    private double                    mutationProbability;
    private int                       eliteCount;
    private List<OptimizerChromosome> initialOptimizerChromosomeList;
    private int                       threadCount;
    private String                    scoreEquation;

    public Optimizer (OptimizerSessionModel optimizerSessionModel, List<OptimizerChromosome> initialOptimizerChromosomeList) {
        this.id = optimizerSessionModel.getId ();
        this.code = optimizerSessionModel.getCode ();
        this.symbol = Symbols.of (optimizerSessionModel.getSymbol ());
        this.period = optimizerSessionModel.getPeriod ();
        this.startDate = optimizerSessionModel.getStartDate ();
        this.endDate = optimizerSessionModel.getEndDate ();
        this.populationSize = optimizerSessionModel.getPopulationSize ();
        this.mutationProbability = optimizerSessionModel.getMutationProbability ();
        this.eliteCount = optimizerSessionModel.getEliteCount ();
        this.threadCount = optimizerSessionModel.getThreadCount ();
        this.initialOptimizerChromosomeList = initialOptimizerChromosomeList;
        this.scoreEquation = optimizerSessionModel.getScoreEquation ();
    }

    public void run () {
        MarketData ohlc = StandaloneMarketDataService.getInstance ().getOHLC (symbol, period, startDate, endDate);

        List<OptimizerDef> optimizerDefList = Algo.getOptimizerDefList (code);

        ScoreCalculator scoreCalculator = new ScoreCalculator (scoreEquation);

        FitnessFunction fitnessFunction = new OptimizerFitnessFunction (code, ohlc, scoreCalculator);

        CrossoverMethod crossoverMethod = new OptimizerCrossover ();

        MutationMethod mutationMethod = new OptimizerMutation (Algo.getOptimizerDefList (code));

        Population<OptimizerChromosome> initialPopulation = initializePopulation (populationSize, optimizerDefList, initialOptimizerChromosomeList);

        GPConfiguration gpConfiguration = new GPConfiguration ();

        gpConfiguration.setNewFittestListener (new NewFittestOptimizerListener (id, code));

        gpConfiguration.setNewGenerationListener (new NewGenerationOptimizerListener ());

        gpConfiguration.setMutationProbability (mutationProbability);

        gpConfiguration.setEliteCount (eliteCount);

        GeneticProgram gp = new GeneticProgram (
                threadCount,
                initialPopulation,
                new TournamentSelection (10),
                crossoverMethod,
                mutationMethod,
                fitnessFunction,
                new FittestValueStopCondition (800),
                gpConfiguration
        );

        gp.evolve ();
    }

    public static Population<OptimizerChromosome> initializePopulation(int populationSize, List<OptimizerDef> optimizerDefList, List<OptimizerChromosome> initialOptimizerChromosomeList){
        List<OptimizerChromosome> individuals = new ArrayList<OptimizerChromosome> (populationSize);

        for (OptimizerChromosome optimizerChromosome : initialOptimizerChromosomeList){
            individuals.add (optimizerChromosome);
        }
        for ( int i = 0; i < populationSize - initialOptimizerChromosomeList.size (); i++ ) {
            Double[] gen = new Double[optimizerDefList.size ()];
            int j = 0;
            for(OptimizerDef optimizerDef : optimizerDefList){
                gen[j++] = Util.getRandomDouble (optimizerDef);
            }
            individuals.add (new OptimizerChromosome (gen));
        }
        return new Population<OptimizerChromosome> (individuals);
    }

    public String getSessionId () {
        return id;
    }

    public String getCode () {
        return code;
    }

    public Symbol getSymbol () {
        return symbol;
    }

    public Period getPeriod () {
        return period;
    }

    public Date getStartDate () {
        return startDate;
    }

    public Date getEndDate () {
        return endDate;
    }

    public int getPopulationSize () {
        return populationSize;
    }

    public double getMutationProbability () {
        return mutationProbability;
    }

    public int getEliteCount () {
        return eliteCount;
    }

    public List<OptimizerChromosome> getInitialOptimizerChromosomeList () {
        return initialOptimizerChromosomeList;
    }

    public int getThreadCount () {
        return threadCount;
    }

    @Override
    public String toString () {
        return "Optimizer{" +
                "id='" + id + '\'' +
                //", code='" + code + '\'' +
                ", symbol=" + symbol +
                ", period=" + period +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", populationSize=" + populationSize +
                ", mutationProbability=" + mutationProbability +
                ", eliteCount=" + eliteCount +
                ", initialOptimizerChromosomeList=" + initialOptimizerChromosomeList +
                ", threadCount=" + threadCount +
                '}';
    }
}
