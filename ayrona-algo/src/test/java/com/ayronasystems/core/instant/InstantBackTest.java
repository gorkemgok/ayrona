package com.ayronasystems.core.instant;

import com.ayronasystems.core.concrete.FatihAlgo;
import com.ayronasystems.core.algo.FunctionFactory;
import com.ayronasystems.core.backtest.*;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.exception.PrerequisiteException;
import com.ayronasystems.core.service.BackTestService;
import com.ayronasystems.core.service.StandaloneBackTestService;
import com.ayronasystems.core.timeseries.moment.ColumnDefinition;
import com.ayronasystems.core.timeseries.moment.EquityBar;
import com.ayronasystems.core.timeseries.series.TimeSeries;
import com.ayronasystems.core.util.DateUtils;
import org.knowm.xchart.*;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.util.Arrays;

/**
 * Created by gorkemgok on 18/05/16.
 */
public class InstantBackTest {

    public static void main(String[] args){
        FunctionFactory.scanFunctions ();
        FatihAlgo algo1 = new FatihAlgo (new double[]{0.002, 0.2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 10, 10, 10, 81});
        FatihAlgo algo2 = new FatihAlgo (new double[]{0.002, 0.2, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 81});
        FatihAlgo algo3 = new FatihAlgo (new double[]{0.002, 0.2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 10, 10, 10, 41});
        FatihAlgo algo4 = new FatihAlgo (new double[]{0.002, 0.2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 10, 10, 10, 20});

        BackTestService bts = new StandaloneBackTestService ();
        try {
            long start = System.currentTimeMillis ();
            BackTestResult result1 = bts.doBackTest (algo1, Symbol.VOB30, Period.M5, DateUtils.parseDate ("01.01.2010 00:00:00"), DateUtils.parseDate ("01.01.2016 00:00:00"));
            BackTestResult result2 = bts.doBackTest (algo2, Symbol.VOB30, Period.M5, DateUtils.parseDate ("01.01.2010 00:00:00"), DateUtils.parseDate ("01.01.2016 00:00:00"));
            BackTestResult result3 = bts.doBackTest (algo3, Symbol.VOB30, Period.M5, DateUtils.parseDate ("01.01.2010 00:00:00"), DateUtils.parseDate ("01.01.2016 00:00:00"));
            BackTestResult result4 = bts.doBackTest (algo4, Symbol.VOB30, Period.M5, DateUtils.parseDate ("01.01.2010 00:00:00"), DateUtils.parseDate ("01.01.2016 00:00:00"));
            long end = System.currentTimeMillis ();
            System.out.println ("Elapsed Time : "+(end - start)+"ms");
            System.out.println (result1);

            drawChart (result1, result2, result3, result4);
        } catch ( PrerequisiteException e ) {
            e.printStackTrace ();
        }
    }

    public static void drawChart(BackTestResult result1,
                                 BackTestResult result2,
                                 BackTestResult result3,
                                 BackTestResult result4){
        TimeSeries<EquityBar> equitySeries1 = (TimeSeries) result1.getResult (MetricType.EQUITY_SERIES).getValue ();
        TimeSeries<EquityBar> equitySeries2 = (TimeSeries) result2.getResult (MetricType.EQUITY_SERIES).getValue ();
        TimeSeries<EquityBar> equitySeries3 = (TimeSeries) result3.getResult (MetricType.EQUITY_SERIES).getValue ();
        TimeSeries<EquityBar> equitySeries4 = (TimeSeries) result4.getResult (MetricType.EQUITY_SERIES).getValue ();

        double[] yData1 = new double[equitySeries1.getMomentCount ()];
        double[] yData2 = new double[equitySeries2.getMomentCount ()];
        double[] yData3 = new double[equitySeries3.getMomentCount ()];
        double[] yData4 = new double[equitySeries4.getMomentCount ()];
        int i = 0;
        for (EquityBar equityBar : equitySeries1){
            yData1[i] = equityBar.get (ColumnDefinition.EQUITY);
            i++;
        }

        i = 0;
        for (EquityBar equityBar : equitySeries2){
            yData2[i] = equityBar.get (ColumnDefinition.EQUITY);
            i++;
        }
        i = 0;
        for (EquityBar equityBar : equitySeries3){
            yData3[i] = equityBar.get (ColumnDefinition.EQUITY);
            i++;
        }
        i = 0;
        for (EquityBar equityBar : equitySeries4){
            yData4[i] = equityBar.get (ColumnDefinition.EQUITY);
            i++;
        }

        // Create Chart
        XYChart chartEquity1 = new XYChartBuilder ().width(900).height(450).title("Stability("+result1.getResult (MetricType.STABILITY).getValue ()+")").xAxisTitle("").yAxisTitle("Value").build();
        XYChart chartEquity2 = new XYChartBuilder ().width(900).height(450).title("Stability("+result2.getResult (MetricType.STABILITY).getValue ()+")").xAxisTitle("").yAxisTitle("Value").build();
        XYChart chartEquity3 = new XYChartBuilder ().width(900).height(450).title("Stability("+result3.getResult (MetricType.STABILITY).getValue ()+")").xAxisTitle("").yAxisTitle("Value").build();
        XYChart chartEquity4 = new XYChartBuilder ().width(900).height(450).title("Stability("+result4.getResult (MetricType.STABILITY).getValue ()+")").xAxisTitle("").yAxisTitle("Value").build();

        XYSeries seriesEq = chartEquity1.addSeries ("Equity", null, yData1);
        seriesEq.setMarker(SeriesMarkers.NONE);

        XYSeries seriesEq2 = chartEquity2.addSeries ("Equity", null, yData2);
        seriesEq2.setMarker(SeriesMarkers.NONE);

        XYSeries seriesEq3 = chartEquity3.addSeries ("Equity", null, yData3);
        seriesEq3.setMarker(SeriesMarkers.NONE);

        XYSeries seriesEq4 = chartEquity4.addSeries ("Equity", null, yData4);
        seriesEq4.setMarker(SeriesMarkers.NONE);

        new SwingWrapper (Arrays.asList (chartEquity1, chartEquity2, chartEquity3, chartEquity4)).displayChartMatrix ();
    }

}
