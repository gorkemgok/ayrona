package com.ayronasystems.genetics.examples.ayronaoptimizer;

import com.ayronasystems.core.algo.FunctionFactory;
import com.ayronasystems.core.dao.model.OptimizerSessionModel;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.util.DateUtils;
import com.ayronasystems.genetics.optimizer.Optimizer;
import com.ayronasystems.genetics.optimizer.OptimizerChromosome;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 07/09/16.
 */
public class Main {

    public static void main(String[] args){
        FunctionFactory.scanFunctions ();

        String code = "Sistem.NAME = \"FatihAlgo1\";\n" +
                "var SAR = Sistem.SAR (Sistem.Optimize(0.001, 0.01, 0.0001), Sistem.Optimize(0.1, 0.9, 0.01));\n" +
                "\n" +
                "var SMA1 = Sistem.SMA (Sistem.C, Sistem.Optimize(2, 40, 1));\n" +
                "var SMA2 = Sistem.SMA (SMA1, Sistem.Optimize(2, 40, 1));\n" +
                "var SMA3 = Sistem.SMA (SMA2, Sistem.Optimize(2, 40, 1));\n" +
                "var SMA4 = Sistem.SMA (SMA3, Sistem.Optimize(2, 40, 1));\n" +
                "var SMA5 = Sistem.SMA (SMA4, Sistem.Optimize(2, 40, 1));\n" +
                "var SMA6 = Sistem.SMA (SMA5, Sistem.Optimize(2, 40, 1));\n" +
                "var SMA7 = Sistem.SMA (SMA6, Sistem.Optimize(2, 40, 1));\n" +
                "var SMA8 = Sistem.SMA (SMA7, Sistem.Optimize(2, 40, 1));\n" +
                "var SMA9 = Sistem.SMA (SMA8, Sistem.Optimize(2, 40, 1));\n" +
                "var SMA10 = Sistem.SMA (SMA9, Sistem.Optimize(2, 40, 1));\n" +
                "\n" +
                "var TOPLAM_SMA = Sistem.ADD (SMA1, SMA2, SMA3, SMA4, SMA5, SMA6, SMA7, SMA8, SMA9, SMA10);\n" +
                "var KAPANIS_EKSI_SMA_BOLU_10 = Sistem.SUB (Sistem.C, Sistem.DIV(TOPLAM_SMA, 10));\n" +
                "\n" +
                "var HHV = Sistem.HIGHEST (Sistem.H, Sistem.Optimize(10, 40, 1));\n" +
                "var LLV = Sistem.LOWEST (Sistem.L, Sistem.Optimize(10, 40, 1));\n" +
                "var HLV_EKSI_LLV = Sistem.SUB (HHV, LLV);\n" +
                "\n" +
                "var ATAFILTRE = Sistem.DIV (KAPANIS_EKSI_SMA_BOLU_10, HLV_EKSI_LLV);\n" +
                "var XX = Sistem.SMA(ATAFILTRE, Sistem.Optimize(20, 150, 5));\n" +
                "\n" +
                "Sistem.BUY = Sistem.AND(Sistem.GT (Sistem.C , SAR), Sistem.GT(XX, 0));\n" +
                "Sistem.SELL = Sistem.AND(Sistem.LT (Sistem.C , SAR), Sistem.LT(XX, 0));";

        OptimizerSessionModel optimizerSessionModel = new OptimizerSessionModel ();
        optimizerSessionModel.setCode (code);
        optimizerSessionModel.setSymbol ("VOB_X30");
        optimizerSessionModel.setPeriod (Period.M5);
        optimizerSessionModel.setStartDate (DateUtils.parseDate ("01.01.2010 00:00:00"));
        optimizerSessionModel.setEndDate (DateUtils.parseDate ("01.01.2015 00:00:00"));
        optimizerSessionModel.setPopulationCount (50);
        optimizerSessionModel.setMutationProbability (0.7);
        optimizerSessionModel.setEliteCount (10);
        optimizerSessionModel.setThreadCount (2);

        List<OptimizerChromosome> initialOptimizerChromosomeList = new ArrayList<OptimizerChromosome> ();
        initialOptimizerChromosomeList.add (new OptimizerChromosome (new Double[]{0.002, 0.2, 2d, 2d, 2d, 2d, 2d, 2d, 2d, 2d, 2d, 2d, 10d, 10d, 81d}));
        initialOptimizerChromosomeList.add (new OptimizerChromosome (new Double[]{0.002, 0.2, 8.0, 18.0, 7.0, 3.0, 6.0, 8.0, 4.0, 4.0, 5.0, 5.0, 25.0, 10.0, 81.0}));
        initialOptimizerChromosomeList.add (new OptimizerChromosome (new Double[]{0.002, 0.5, 8.0, 18.0, 7.0, 3.0, 2.0, 13.0, 4.0, 4.0, 5.0, 4.0, 25.0, 10.0, 81.0}));
        initialOptimizerChromosomeList.add (new OptimizerChromosome (new Double[]{0.002, 0.4, 11.0, 16.0, 7.0, 3.0, 4.0, 13.0, 4.0, 4.0, 6.0, 4.0, 25.0, 10.0, 81.0}));

        Optimizer optimizer = new Optimizer (optimizerSessionModel, initialOptimizerChromosomeList);
        optimizer.start ();

    }

}
