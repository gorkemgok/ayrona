package com.ayronasystems.genetics.examples.ayronaoptimizer;

import com.ayronasystems.core.algo.Algo;
import com.ayronasystems.core.algo.FunctionFactory;
import com.ayronasystems.core.algo.OptimizerDef;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbols;
import com.ayronasystems.core.service.StandaloneMarketDataService;
import com.ayronasystems.core.util.DateUtils;
import com.ayronasystems.genetics.core.listener.NewGenerationListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 22/07/16.
 */
public class Optimizer {

    public static void main(String[] args){
        FunctionFactory.scanFunctions ();

        String code = "Sistem.NAME = \"FatihAlgo1\";\n" +
                "var SAR = Sistem.SAR (Sistem.Optimize(0.001, 0.005, 0.0005), Sistem.Optimize(0.1, 0.8, 0.05));\n" +
                "\n" +
                "var SMA1 = Sistem.SMA (Sistem.C, Sistem.Optimize(2, 20, 1));\n" +
                "var SMA2 = Sistem.SMA (SMA1, Sistem.Optimize(2, 20, 1));\n" +
                "var SMA3 = Sistem.SMA (SMA2, Sistem.Optimize(2, 20, 1));\n" +
                "var SMA4 = Sistem.SMA (SMA3, Sistem.Optimize(2, 20, 1));\n" +
                "var SMA5 = Sistem.SMA (SMA4, Sistem.Optimize(2, 20, 1));\n" +
                "var SMA6 = Sistem.SMA (SMA5, Sistem.Optimize(2, 20, 1));\n" +
                "var SMA7 = Sistem.SMA (SMA6, Sistem.Optimize(2, 20, 1));\n" +
                "var SMA8 = Sistem.SMA (SMA7, Sistem.Optimize(2, 20, 1));\n" +
                "var SMA9 = Sistem.SMA (SMA8, Sistem.Optimize(2, 20, 1));\n" +
                "var SMA10 = Sistem.SMA (SMA9, Sistem.Optimize(2, 20, 1));\n" +
                "\n" +
                "var TOPLAM_SMA = Sistem.ADD (SMA1, SMA2, SMA3, SMA4, SMA5, SMA6, SMA7, SMA8, SMA9, SMA10);\n" +
                "var KAPANIS_EKSI_SMA_BOLU_10 = Sistem.SUB (Sistem.C, Sistem.DIV(TOPLAM_SMA, 10));\n" +
                "\n" +
                "var HHV = Sistem.HIGHEST (Sistem.H, Sistem.Optimize(20, 30, 5));\n" +
                "var LLV = Sistem.LOWEST (Sistem.L, Sistem.Optimize(20, 30, 5));\n" +
                "var HLV_EKSI_LLV = Sistem.SUB (HHV, LLV);\n" +
                "\n" +
                "var ATAFILTRE = Sistem.DIV (KAPANIS_EKSI_SMA_BOLU_10, HLV_EKSI_LLV);\n" +
                "var XX = Sistem.SMA(ATAFILTRE, Sistem.Optimize(20, 150, 5));\n" +
                "\n" +
                "Sistem.BUY = Sistem.AND(Sistem.GT (Sistem.C , SAR), Sistem.GT(XX, 0));\n" +
                "Sistem.SELL = Sistem.AND(Sistem.LT (Sistem.C , SAR), Sistem.LT(XX, 0));";

        MarketData ohlc = StandaloneMarketDataService.getInstance ().getOHLC (
                Symbols.of ("VOB_X30"), Period.M5,
                DateUtils.parseDate ("01.01.2014 00:00:00"),
                DateUtils.parseDate ("01.01.2015 00:00:00")
        );

        List<OptimizerDef> optimizerDefList = Algo.getOptimizerDefList (code);

        ScoreCalculator scoreCalculator = new ScoreCalculator ();

        FitnessFunction fitnessFunction = new OptimizerFitnessFunction (code, ohlc, scoreCalculator);

        CrossoverMethod crossoverMethod = new OptimizerCrossover ();

        MutationMethod mutationMethod = new OptimizerMutation (Algo.getOptimizerDefList (code));

        Population<OptimizerChromosome> initialPopulation = initializePopulation (500, optimizerDefList);

        GPConfiguration gpConfiguration = new GPConfiguration ();

        gpConfiguration.setNewFittestListener (new NewFittestOptimizerListener ());

        gpConfiguration.setNewGenerationListener (new NewGenerationListener () {
            public void onNewGeneration (Population population) {
                System.out.println (population.getGenerationCount ()+".Gen:"+population.getFittest ());
            }
        });

        gpConfiguration.setMutationProbability (0.8);

        gpConfiguration.setEliteCount (10);

        GeneticProgram gp = new GeneticProgram (
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

    public static Population<OptimizerChromosome> initializePopulation(int populationSize, List<OptimizerDef> optimizerDefList){
        List<OptimizerChromosome> individuals = new ArrayList<OptimizerChromosome> (populationSize);
        individuals.add (new OptimizerChromosome (new Double[]{0.002, 0.2, 2d, 2d, 2d, 2d, 2d, 2d, 2d, 2d, 2d, 2d, 10d, 10d, 81d}));
        individuals.add (new OptimizerChromosome (new Double[]{0.002, 0.2, 8.0, 18.0, 7.0, 3.0, 6.0, 8.0, 4.0, 4.0, 5.0, 5.0, 25.0, 10.0, 81.0}));
        individuals.add (new OptimizerChromosome (new Double[]{0.002, 0.5, 8.0, 18.0, 7.0, 3.0, 2.0, 13.0, 4.0, 4.0, 5.0, 4.0, 25.0, 10.0, 81.0}));
        individuals.add (new OptimizerChromosome (new Double[]{0.002, 0.4, 11.0, 16.0, 7.0, 3.0, 4.0, 13.0, 4.0, 4.0, 6.0, 4.0, 25.0, 10.0, 81.0}));
        for ( int i = 0; i < populationSize - 4; i++ ) {
            Double[] gen = new Double[optimizerDefList.size ()];
            int j = 0;
            for(OptimizerDef optimizerDef : optimizerDefList){
                gen[j++] = Util.getRandomDouble (optimizerDef);
            }
            individuals.add (new OptimizerChromosome (gen));
        }
        return new Population<OptimizerChromosome> (individuals);
    }

}
