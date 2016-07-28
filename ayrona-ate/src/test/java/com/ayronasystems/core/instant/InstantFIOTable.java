package com.ayronasystems.core.instant;

import com.ayronasystems.core.algo.Algo;
import com.ayronasystems.core.algo.FIOExchange;
import com.ayronasystems.core.algo.FunctionFactory;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Signal;
import com.ayronasystems.core.definition.Symbols;
import com.ayronasystems.core.exception.PrerequisiteException;
import com.ayronasystems.core.service.MarketDataService;
import com.ayronasystems.core.service.StandaloneMarketDataService;
import com.ayronasystems.core.util.DateUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by gorkemgok on 24/07/16.
 */
public class InstantFIOTable {

    public static void main(String[] args){
        FunctionFactory.scanFunctions ();
        DecimalFormat df = new DecimalFormat ("##.####");
        String code =
                "var SMA_3 = Sistem.SMA(Sistem.C,3);" +
                "var SMA_5 = Sistem.SMA(Sistem.C, 5);" +
                "var SAR = Sistem.SAR(0.02, 0.2);" +
                "Sistem.BUY = Sistem.GT(SMA_5, SMA_3);" +
                "Sistem.SELL = Sistem.LT(SMA_5, SAR);";
        code = "var SMA_3 = Sistem.SMA(Sistem.C,3);" +
               "var SMA_5 = Sistem.SMA(Sistem.C, 5);" +
               "var SAR = Sistem.SAR(0.02, 0.2);" +
               "Sistem.BUY = Sistem.GT(SMA_3, SMA_5);" +
               "Sistem.SELL = Sistem.LT(SMA_3, SMA_5);";
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
        Algo signalGenerator = Algo.createInstance (code);
        MarketDataService marketDataService = StandaloneMarketDataService.getInstance ();
        MarketData baseMarketData = marketDataService.getOHLC (
                Symbols.of ("VOB_X030"), Period.M5,
                DateUtils.parseDate ("01.01.2015 00:00:00"),
                DateUtils.parseDate ("01.01.2017 00:00:00")
        );

        try {
            List<Signal> btSignals = signalGenerator.getSignalList (baseMarketData);
            Map<String, FIOExchange> fioExchangeMap = signalGenerator.getFioTable ().getTable ();
            StringBuilder hsb = new StringBuilder ();
            List<FIOExchange> fioExchangeList = new ArrayList<FIOExchange> ();
            for ( Map.Entry<String, FIOExchange> entrySet : fioExchangeMap.entrySet ()){
                hsb.append (entrySet.getKey ()).append ("\n");
                fioExchangeList.add (entrySet.getValue ());
            }

            StringBuilder sb = new StringBuilder ("Function IO Table").append ("\n").append (hsb.toString ());
            for ( int i = 1; i <= btSignals.size (); i++ ) {
                int ln = baseMarketData.size () - i;
                Date date = baseMarketData.getDate (ln);
                Signal signal = btSignals.get (btSignals.size () - i);
                sb.append (ln).append (".").append (date).append ("\t\t")
                  .append (String.format ("%-20s",signal)).append ("\t\t");
                for (FIOExchange fioExchange : fioExchangeList){
                    for(double[] data : fioExchange.getData ()){
                        sb.append (
                                String.format ("%-10s",df.format (data[data.length - i]))
                        );
                    }
                    sb.append ("\t\t");
                }
                sb.append ("\n");
            }
            //System.out.print (sb.toString ());
            InstantUtil.copyToClipboard (sb.toString ());
        } catch ( PrerequisiteException e ) {
            e.printStackTrace ();
        }
    }
}
