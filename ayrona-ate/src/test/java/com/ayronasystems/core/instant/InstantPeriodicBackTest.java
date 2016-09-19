package com.ayronasystems.core.instant;

import com.ayronasystems.core.algo.Algo;
import com.ayronasystems.core.algo.FunctionFactory;
import com.ayronasystems.core.backtest.*;
import com.ayronasystems.core.backtest.score.PeriodicSharpeSortinoCalculator;
import com.ayronasystems.core.backtest.score.SharpeSortinoType;
import com.ayronasystems.core.backtest.score.WMASharpeSortinoCalculator;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbols;
import com.ayronasystems.core.exception.PrerequisiteException;
import com.ayronasystems.core.service.StandaloneMarketDataService;
import com.ayronasystems.core.strategy.Position;
import com.ayronasystems.core.strategy.SignalGenerator;
import com.ayronasystems.core.util.DateUtils;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by gorkemgok on 29/08/16.
 */
public class InstantPeriodicBackTest {

    public static DecimalFormat df = new DecimalFormat ("##.##");

    public static void main(String[] args) throws PrerequisiteException {
        FunctionFactory.scanFunctions ();
        String code = "Sistem.NAME = \"FatihAlgo1\";\n" +
                "Sistem.OPT(\n" +
                "    [0.002, 0.2, 8.0, 18.0, 7.0, 3.0, 6.0, 8.0, 4.0, 4.0, 5.0, 5.0, 25.0, 10.0, 81.0]\n" +
                //"[0.002, 0.35, 18.0, 2.0, 17.0, 11.0, 13.0, 14.0, 4.0, 19.0, 15.0, 13.0, 25.0, 25.0, 110.0]"+
                //"[0.002, 0.2, 8.0, 18.0, 7.0, 3.0, 6.0, 8.0, 4.0, 4.0, 5.0, 4.0, 25.0, 10.0, 81.0]"+
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

        MarketData ohlc = StandaloneMarketDataService.getInstance ()
                                                           .getOHLC (Symbols.of("VOB_X30"), Period.M5,
                                                                     DateUtils.parseDate ("01.01.2010 00:00:00"),
                                                                     DateUtils.parseDate ("01.01.2017 00:00:00"));
        long start = System.currentTimeMillis ();
        SignalGenerator signalGenerator = Algo.createInstance (code);
        PositionGenerator positionGenerator = new PositionGenerator (signalGenerator);
        List<Position> positionList = positionGenerator.generate (ohlc);
        BackTestCalculator calculator = new PeriodicBackTestCalculator ();
        BackTestResult result = calculator.calculate (positionList, ohlc);
        long end = System.currentTimeMillis ();

        listResultMap (result.getPeriodicResultMap (), ResultPeriod.MONTH);
        listResultMap (result.getPeriodicResultMap (), ResultPeriod.YEAR);
        listResultMap (result.getPeriodicResultMap (), ResultPeriod.INFINITY);

        System.out.println ((end-start)+" ms");

        Map<Date, Double> sharpeMap;
        Map<Date, Double> sortinoMap;

        PeriodicSharpeSortinoCalculator pSharpeCalculator = new PeriodicSharpeSortinoCalculator (ResultPeriod.YEAR, SharpeSortinoType.SHARPE);
        PeriodicSharpeSortinoCalculator pSortinoCalculator = new PeriodicSharpeSortinoCalculator (ResultPeriod.YEAR, SharpeSortinoType.SORTINO);

        sharpeMap = pSharpeCalculator.calculate (result);
        sortinoMap = pSortinoCalculator.calculate (result);

        DecimalFormat df2 = new DecimalFormat ("##.####");
        for (Map.Entry<Date, Double> entry : sharpeMap.entrySet ()){
            System.out.println (entry.getKey ()+" - "+df2.format (entry.getValue ()));
        }
        System.out.println();
        for (Map.Entry<Date, Double> entry : sortinoMap.entrySet ()){
            System.out.println (entry.getKey ()+" - "+df2.format (entry.getValue ()));
        }

        System.out.println();

        WMASharpeSortinoCalculator sharpeCalculator = new WMASharpeSortinoCalculator (ResultPeriod.YEAR, SharpeSortinoType.SHARPE);
        WMASharpeSortinoCalculator sortinoCalculator = new WMASharpeSortinoCalculator (ResultPeriod.YEAR, SharpeSortinoType.SORTINO);

        double sharpe = sharpeCalculator.calculate (result);
        double sortino = sortinoCalculator.calculate (result);

        System.out.println("WMA Sharpe: "+sharpe);
        System.out.println("WMA Sortino: "+sortino);
    }

    public static void listResultMap(Map<ResultPeriod, List<ResultQuanta>> prm, ResultPeriod resultPeriod){
        List<ResultQuanta> resultQuantaList = prm.get (resultPeriod);
        for (ResultQuanta resultQuanta : resultQuantaList){

            System.out.println (toString (
                    resultQuanta,
                    ResultQuantaMetric.EQUITY, ResultQuantaMetric.EQUITY_PERCENTAGE,
                    ResultQuantaMetric.NET_PROFIT, ResultQuantaMetric.NET_PROFIT_PERCENTAGE,
                    ResultQuantaMetric.MDD, ResultQuantaMetric.MDD_PERCENTAGE
            ));
        }
        System.out.println ();
    }

    public static String toString(ResultQuanta resultQuanta, ResultQuantaMetric... resultQuantaMetrics){
        Date date = resultQuanta.getDate ();
        Calendar calendar = Calendar.getInstance ();
        calendar.setTime (date);
        int year = calendar.get (Calendar.YEAR);
        int month = calendar.get (Calendar.MONTH);
        StringBuilder sb = new StringBuilder (String.valueOf (year)).append ("-").append (String.valueOf (month+1)).append ("\t");
        for (ResultQuantaMetric resultQuantaMetric : resultQuantaMetrics) {
            sb.append (resultQuantaMetric)
              .append (" = ")
              .append (String.format ("%-6s", df.format (resultQuanta.getValue (resultQuantaMetric))))
              .append ("\t");
        }
        return sb.toString ();
    }

}
