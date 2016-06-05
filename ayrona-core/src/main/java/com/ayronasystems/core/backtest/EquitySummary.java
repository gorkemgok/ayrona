package com.ayronasystems.core.backtest;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.timeseries.moment.ColumnDefinition;
import com.ayronasystems.core.timeseries.moment.EquityBar;
import com.ayronasystems.core.timeseries.moment.Moment;
import com.ayronasystems.core.timeseries.series.BasicTimeSeries;
import com.ayronasystems.core.timeseries.series.SeriesIterator;
import com.ayronasystems.core.timeseries.series.SymbolTimeSeries;
import com.ayronasystems.core.timeseries.series.TimeSeries;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by gorkemgok on 10/01/16.
 */
public class EquitySummary extends AbstractSummary {

    public EquitySummary (Date startDate, Date endDate) {
        super (startDate, endDate);
    }

    public void setResult(TimeSeries<EquityBar> equitySeries){
        setResult (MetricType.EQUITY_SERIES, new MetricValue<TimeSeries> (equitySeries));
    }

    public MetricType[] getSummarizedMetricTypes () {
        return new MetricType[]{MetricType.EQUITY_SERIES};
    }

    public static EquitySummary generateDailyEquitySummary(EquitySummary equitySummary){
        TimeSeries<EquityBar> equitySeries = (TimeSeries<EquityBar>)equitySummary.getResult (MetricType.EQUITY_SERIES).getValue ();
        TimeSeries<EquityBar> dailyEquitySeries = BasicTimeSeries.getDynamicSizeInstance (EquityBar.class);
        Date lastDate = null;
        double lastEquity = 0;
        double totalInstantProfit = 0;
        double minMDD = Double.MAX_VALUE;
        for (Moment moment : equitySeries){
            Date currDate = new DateTime (moment.getDate ()).withTime (0, 0, 0, 0).toDate ();
            if ( currDate.equals (lastDate) ) {
            } else {
                if (lastDate != null){
                    EquityBar equityBar = new EquityBar (lastDate, lastEquity, minMDD, totalInstantProfit);
                    dailyEquitySeries.addMoment (equityBar);
                    minMDD = Double.MAX_VALUE;
                    totalInstantProfit = 0;
                }
                lastDate = currDate;
            }
            lastEquity = moment.get (ColumnDefinition.EQUITY);
            minMDD = Math.min (minMDD, moment.get (ColumnDefinition.MDD));
            totalInstantProfit += moment.get (ColumnDefinition.INSTANTPROFIT);
        }
        EquitySummary dailyEquitySummary = new EquitySummary (equitySummary.getStartDate (), equitySummary.getEndDate ());
        dailyEquitySummary.setResult (dailyEquitySeries);
        return dailyEquitySummary;
    }
}
