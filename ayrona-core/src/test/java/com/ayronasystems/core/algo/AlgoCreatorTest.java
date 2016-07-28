package com.ayronasystems.core.algo;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by gorkemgok on 17/07/16.
 */
public class AlgoCreatorTest {

    @Test
    public void optimize () throws Exception {
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
                "var HHV = Sistem.HIGHEST (Sistem.H, Sistem.Optimize(10, 50, 1));\n" +
                "var LLV = Sistem.LOWEST (Sistem.L, Sistem.Optimize(10, 50, 1));\n" +
                "var HLV_EKSI_LLV = Sistem.SUB (HHV, LLV);\n" +
                "\n" +
                "var ATAFILTRE = Sistem.DIV (KAPANIS_EKSI_SMA_BOLU_10, HLV_EKSI_LLV);\n" +
                "var XX = Sistem.SMA(ATAFILTRE, Sistem.Optimize(20, 150, 5));\n" +
                "\n" +
                "Sistem.BUY = Sistem.AND(Sistem.GT (Sistem.C , SAR), Sistem.GT(XX, 0));\n" +
                "Sistem.SELL = Sistem.AND(Sistem.LT (Sistem.C , SAR), Sistem.LT(XX, 0));";

        List<OptimizerDef> optimizerDefList = Algo.getOptimizerDefList (code);

        assertEquals (15, optimizerDefList.size ());
        assertEquals (0.001, optimizerDefList.get (0).getStart (), 0);
        assertEquals (0.005, optimizerDefList.get (0).getEnd (), 0);
        assertEquals (0.0005, optimizerDefList.get (0).getStep (), 0);

        assertEquals (0.1, optimizerDefList.get (1).getStart (), 0);
        assertEquals (0.8, optimizerDefList.get (1).getEnd (), 0);
        assertEquals (0.05, optimizerDefList.get (1).getStep (), 0);

        for ( int i = 2; i < 12; i++ ) {
            assertEquals (2, optimizerDefList.get (i).getStart (), 0);
            assertEquals (20, optimizerDefList.get (i).getEnd (), 0);
            assertEquals (1, optimizerDefList.get (i).getStep (), 0);
        }

        assertEquals (10, optimizerDefList.get (12).getStart (), 0);
        assertEquals (50, optimizerDefList.get (12).getEnd (), 0);
        assertEquals (1, optimizerDefList.get (12).getStep (), 0);

        assertEquals (10, optimizerDefList.get (13).getStart (), 0);
        assertEquals (50, optimizerDefList.get (13).getEnd (), 0);
        assertEquals (1, optimizerDefList.get (13).getStep (), 0);

        assertEquals (20, optimizerDefList.get (14).getStart (), 0);
        assertEquals (150, optimizerDefList.get (14).getEnd (), 0);
        assertEquals (5, optimizerDefList.get (14).getStep (), 0);

        Double[] optimizedValues = new Double[]{
                0.002, 0.2, 2d, 2d, 2d, 2d, 2d, 2d, 2d, 2d, 2d, 2d, 10d, 10d, 81d };

        String algoO = Algo.createInstance (code, optimizedValues, "").toString ();

        code = "Sistem.NAME = \"FatihAlgo1\";\n" +
                "var SAR = Sistem.SAR (0.002, 0.2);\n" +
                "\n" +
                "var SMA1 = Sistem.SMA (Sistem.C, 2);\n" +
                "var SMA2 = Sistem.SMA (SMA1, 2);\n" +
                "var SMA3 = Sistem.SMA (SMA2, 2);\n" +
                "var SMA4 = Sistem.SMA (SMA3, 2);\n" +
                "var SMA5 = Sistem.SMA (SMA4, 2);\n" +
                "var SMA6 = Sistem.SMA (SMA5, 2);\n" +
                "var SMA7 = Sistem.SMA (SMA6, 2);\n" +
                "var SMA8 = Sistem.SMA (SMA7, 2);\n" +
                "var SMA9 = Sistem.SMA (SMA8, 2);\n" +
                "var SMA10 = Sistem.SMA (SMA9, 2);\n" +
                "\n" +
                "var TOPLAM_SMA = Sistem.ADD (SMA1, SMA2, SMA3, SMA4, SMA5, SMA6, SMA7, SMA8, SMA9, SMA10);\n" +
                "var KAPANIS_EKSI_SMA_BOLU_10 = Sistem.SUB (Sistem.C, Sistem.DIV(TOPLAM_SMA, 10));\n" +
                "\n" +
                "var HHV = Sistem.HIGHEST (Sistem.H, 10);\n" +
                "var LLV = Sistem.LOWEST (Sistem.L, 10);\n" +
                "var HLV_EKSI_LLV = Sistem.SUB (HHV, LLV);\n" +
                "\n" +
                "var ATAFILTRE = Sistem.DIV (KAPANIS_EKSI_SMA_BOLU_10, HLV_EKSI_LLV);\n" +
                "var XX = Sistem.SMA(ATAFILTRE, 81);\n" +
                "\n" +
                "Sistem.BUY = Sistem.AND(Sistem.GT (Sistem.C , SAR), Sistem.GT(XX, 0));\n" +
                "Sistem.SELL = Sistem.AND(Sistem.LT (Sistem.C , SAR), Sistem.LT(XX, 0));";

        String algo = Algo.createInstance (code).toString ();

        assertEquals (algo, algoO);

    }

}