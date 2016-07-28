package com.ayronasystems.core.instant;

import com.ayronasystems.core.Position;
import com.ayronasystems.core.algo.FunctionFactory;
import com.ayronasystems.core.backtest.BackTestResult;
import com.ayronasystems.core.backtest.MetricType;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbols;
import com.ayronasystems.core.exception.PrerequisiteException;
import com.ayronasystems.core.service.BackTestService;
import com.ayronasystems.core.service.StandaloneBackTestService;
import com.ayronasystems.core.util.DateUtils;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 01/06/16.
 */
public class InstantBackTestSimple {

    public static void main(String[] args) throws PrerequisiteException {
        FunctionFactory.scanFunctions ();
        String code =
                "var SMA_20 = Sistem.SMA(Sistem.C,20);" +
                "var SMA_5 = Sistem.SMA(Sistem.C, 5);" +
                "Sistem.BUY = Sistem.GT(SMA_5, SMA_20);" +
                "Sistem.SELL = Sistem.LT(SMA_5, SMA_20);";
        code = "Sistem.NAME = \"FatihAlgo1\";\n" +
                "Sistem.OPT(\n" +
                "    [0.002, 0.2, 8.0, 18.0, 7.0, 3.0, 6.0, 8.0, 4.0, 4.0, 5.0, 5.0, 25.0, 10.0, 81.0]\n" +
                ");\n" +
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
        BackTestService bts = new StandaloneBackTestService ();
        BackTestResult result = bts.doBackTest (code, Symbols.of("VOB_X030"), Period.M5,
                                                DateUtils.parseDate ("01.01.2010 00:00:00"),
                                                DateUtils.parseDate ("01.01.2017 00:00:00")
        );
        //BackTestResult result2 = bts.doSimulationBackTest (code, Symbols.of("TEST"), Period.M5, null, null);

        System.out.println (result.getResult (MetricType.NET_PROFIT).getValue ());
        //System.out.println (result2.getResult (MetricType.NET_PROFIT).getValue ());

        DecimalFormat df = new DecimalFormat ("##.####");
        System.out.println (result);
        List<Position> positionList = result.getPositionList ();
        StringBuilder sb = new StringBuilder ();
        for (Position position : positionList){
            Date closeDate = position.getCloseDate ();
            sb.append (DateUtils.formatDate (position.getOpenDate ())).append ("\t")
              .append (position.getOpenPrice ()).append ("\t")
              .append (closeDate != null ? DateUtils.formatDate (position.getCloseDate ()) : "ACIK").append ("\t")
              .append (position.getClosePrice ()).append ("\t")
              .append (df.format (position.calculateProfit ())).append ("\n");
        }
        InstantUtil.copyToClipboard (sb.toString ());
    }
}
