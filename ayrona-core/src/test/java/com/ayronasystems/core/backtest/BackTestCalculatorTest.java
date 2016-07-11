package com.ayronasystems.core.backtest;

import com.ayronasystems.core.Position;
import com.ayronasystems.core.data.OHLC;
import com.ayronasystems.core.definition.Direction;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbols;
import com.ayronasystems.core.exception.CorruptedMarketDataException;
import com.ayronasystems.core.timeseries.moment.ColumnDefinition;
import com.ayronasystems.core.timeseries.moment.EquityBar;
import com.ayronasystems.core.timeseries.series.TimeSeries;
import com.ayronasystems.core.util.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by gorkemgok on 22/05/16.
 */
public class BackTestCalculatorTest {

    public static final double DELTA = 0.01;

    private List<Date> marketDates = Arrays.asList (new Date[]{
            DateUtils.parseDate ("01.01.2016 00:00:00"),
            DateUtils.parseDate ("01.01.2016 00:05:00"),
            DateUtils.parseDate ("01.01.2016 00:10:00"),
            DateUtils.parseDate ("01.01.2016 00:15:00"),
            DateUtils.parseDate ("01.01.2016 00:20:00"),
            DateUtils.parseDate ("01.01.2016 00:25:00"),
            DateUtils.parseDate ("01.01.2016 00:30:00"),
            DateUtils.parseDate ("01.01.2016 00:35:00"),
            DateUtils.parseDate ("01.01.2016 00:40:00"),
            DateUtils.parseDate ("01.01.2016 00:45:00"),
            DateUtils.parseDate ("01.01.2016 00:50:00"),
            DateUtils.parseDate ("01.01.2016 00:55:00"),
            DateUtils.parseDate ("01.01.2016 01:00:00")
    });

    private double[] series1 = new double[]{1.0, 1.2, 1.1, 1.5, 1.6, 2.0, 1.3, 1.8, 1.2, 1.4, 1.1, 1.9, 1.5};
    private double[] series2 = new double[]{.8, 1.0, .9, 1.3, 1.4, 1.8, 1.1, 1.6, 1.0, 1.2, .9, 1.7, 1.3};


    private OHLC ohlc;
    private List<Position> positionList;

    @Before
    public void setup(){
        try {
            ohlc = new OHLC (Symbols.of ("TEST"), Period.M5, marketDates, series1, series1, series2, series1);
            positionList = new ArrayList<Position> ();

            Position position = Position.builder (null)
                    .direction (Direction.SHORT)
                    .lot (1)
                    .openDate (marketDates.get (0))
                    .openPrice (series1[0]).build ();
            position.close (marketDates.get (3), series1[3]);

            positionList.add (position);

            position = Position.builder (null)
                               .direction (Direction.SHORT)
                               .lot (1)
                               .openDate (marketDates.get (3))
                               .openPrice (series1[3]).build ();
            position.close (marketDates.get (6), series1[6]);

            positionList.add (position);

            position = Position.builder (null)
                               .direction (Direction.LONG)
                               .lot (1)
                               .openDate (marketDates.get (6))
                               .openPrice (series1[6]).build ();
            position.close (marketDates.get (8), series1[8]);

            positionList.add (position);
        } catch ( CorruptedMarketDataException e ) {
            e.printStackTrace ();
        }
    }

    @Test
    public void calculate () throws Exception {
        BackTestCalculator calculator = new BackTestCalculator ();
        BackTestResult result = calculator.calculate (positionList, ohlc);
        double netProfit = (Double)result.getResult (MetricType.NET_PROFIT).getValue ();
        double mdd = (Double)result.getResult (MetricType.MAX_TRADE_DRAWDOWN).getValue ();
        TimeSeries<EquityBar> equitySeries = (TimeSeries)result.getResult (MetricType.EQUITY_SERIES).getValue ();

        assertEquals (-0.4, netProfit, 0.01);
        assertEquals (-0.4, mdd, 0.01);
        assertEquals (0, equitySeries.getMoment (0).get (ColumnDefinition.EQUITY), DELTA);
        assertEquals (0, equitySeries.getMoment (1).get (ColumnDefinition.EQUITY), DELTA);
        assertEquals (0, equitySeries.getMoment (2).get (ColumnDefinition.EQUITY), DELTA);
        assertEquals (-.5, equitySeries.getMoment (3).get (ColumnDefinition.EQUITY), DELTA);
        assertEquals (-.5, equitySeries.getMoment (4).get (ColumnDefinition.EQUITY), DELTA);
        assertEquals (-.5, equitySeries.getMoment (5).get (ColumnDefinition.EQUITY), DELTA);
        assertEquals (-.3, equitySeries.getMoment (6).get (ColumnDefinition.EQUITY), DELTA);
        assertEquals (-.3, equitySeries.getMoment (7).get (ColumnDefinition.EQUITY), DELTA);
        assertEquals (-.4, equitySeries.getMoment (8).get (ColumnDefinition.EQUITY), DELTA);

        assertEquals (0, equitySeries.getMoment (0).get (ColumnDefinition.INSTANTPROFIT), DELTA);
        assertEquals (0, equitySeries.getMoment (1).get (ColumnDefinition.INSTANTPROFIT), DELTA);
        assertEquals (0, equitySeries.getMoment (2).get (ColumnDefinition.INSTANTPROFIT), DELTA);
        assertEquals (-.5, equitySeries.getMoment (3).get (ColumnDefinition.INSTANTPROFIT), DELTA);
        assertEquals (0, equitySeries.getMoment (4).get (ColumnDefinition.INSTANTPROFIT), DELTA);
        assertEquals (0, equitySeries.getMoment (5).get (ColumnDefinition.INSTANTPROFIT), DELTA);
        assertEquals (.2, equitySeries.getMoment (6).get (ColumnDefinition.INSTANTPROFIT), DELTA);
        assertEquals (0, equitySeries.getMoment (7).get (ColumnDefinition.INSTANTPROFIT), DELTA);
        assertEquals (-.1, equitySeries.getMoment (8).get (ColumnDefinition.INSTANTPROFIT), DELTA);
    }

}