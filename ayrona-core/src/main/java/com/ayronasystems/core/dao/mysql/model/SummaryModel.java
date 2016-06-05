package com.ayronasystems.core.dao.mysql.model;

import com.ayronasystems.core.backtest.EquitySummary;
import com.ayronasystems.core.backtest.MetricType;
import com.ayronasystems.core.backtest.Summary;
import com.ayronasystems.core.definition.BackTestType;
import com.ayronasystems.core.timeseries.moment.ColumnDefinition;
import com.ayronasystems.core.timeseries.moment.EquityBar;
import com.ayronasystems.core.timeseries.moment.Moment;
import com.ayronasystems.core.timeseries.series.SeriesIterator;
import com.ayronasystems.core.timeseries.series.SymbolTimeSeries;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gorkemgok on 10/01/16.
 */
@XmlRootElement
@Entity
@Table(name = "summary")
public class SummaryModel extends BaseModel{

    private static boolean generateDailyEquitySummaryIfAvailable = true;

    private Date startDate;

    private Date endDate;

    private Set<ResultModel> results = new HashSet<ResultModel> ();

    private StrategyModel strategy;

    private Class summaryType;

    private BackTestType backTestType;

    @XmlTransient
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "strategy_id")
    public StrategyModel getStrategy () {
        return strategy;
    }

    public void setStrategy (StrategyModel strategy) {
        this.strategy = strategy;
    }

    @Column(name = "start_date")
    public Date getStartDate () {
        return startDate;
    }

    public void setStartDate (Date startDate) {
        this.startDate = startDate;
    }

    @Column(name = "end_date")
    public Date getEndDate () {
        return endDate;
    }

    public void setEndDate (Date endDate) {
        this.endDate = endDate;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "summary_id", nullable = false)
    @OrderBy("date")
    public Set<ResultModel> getResults () {
        return results;
    }

    public void setResults (Set<ResultModel> results) {
        this.results = results;
    }

    @Column(name = "summary_type", nullable = false)
    public Class getSummaryType () {
        return summaryType;
    }

    public void setSummaryType (Class summaryType) {
        this.summaryType = summaryType;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "interval_type", nullable = false)
    public BackTestType getBackTestType () {
        return backTestType;
    }

    public void setBackTestType (BackTestType backTestType) {
        this.backTestType = backTestType;
    }

    public static void generateDailyEquitySummaryIfAvailable(boolean value){
        generateDailyEquitySummaryIfAvailable = value;
    }

    public static SummaryModel valueOf(Summary summary){
        SummaryModel summaryModel = new SummaryModel ();
        summaryModel.setStartDate (summary.getStartDate ());
        summaryModel.setEndDate (summary.getEndDate ());
        summaryModel.setSummaryType (summary.getClass ());

        Set<ResultModel> resultModelSet = new HashSet<ResultModel> ();

        for (MetricType metricType : summary.getSummarizedMetricTypes ()){

            if (metricType.getValueType ().equals (Double.TYPE)) {
                double value = summary.getResultAsDouble (metricType);
                if (!Double.isNaN (value) && !Double.isInfinite (value)) {
                    ResultModel resultModel = new ResultModel ();
                    resultModel.setMetricType (metricType);
                    resultModel.setValue (value);
                    resultModelSet.add (resultModel);
                }
            }else if (metricType.getValueType ().equals (Integer.TYPE)) {
                ResultModel resultModel = new ResultModel ();
                resultModel.setMetricType (metricType);
                resultModel.setValue (summary.getResultAsInteger (metricType));
                resultModelSet.add (resultModel);
            }else if (metricType.getValueType ().equals (SymbolTimeSeries.class)){
                EquitySummary equitySummaryToBeGenerated;
                if (generateDailyEquitySummaryIfAvailable){
                    equitySummaryToBeGenerated = EquitySummary.generateDailyEquitySummary ((EquitySummary) summary);
                }else {
                    equitySummaryToBeGenerated = (EquitySummary)summary;
                }
                SymbolTimeSeries<EquityBar> equityBar = (SymbolTimeSeries<EquityBar>) equitySummaryToBeGenerated.getResult (metricType).getValue ();
                SeriesIterator iterator = equityBar.begin ();
                while ( iterator.hasNext () ){
                    Moment moment = iterator.next ();

                    ResultModel resultModel = new ResultModel ();
                    resultModel.setMetricType (MetricType.EQUITY_SERIES_EQUITY);
                    resultModel.setDate (moment.getDate ());
                    resultModel.setValue (moment.get (ColumnDefinition.EQUITY));
                    resultModelSet.add (resultModel);

                    resultModel = new ResultModel ();
                    resultModel.setMetricType (MetricType.EQUITY_SERIES_MDD);
                    resultModel.setDate (moment.getDate ());
                    resultModel.setValue (moment.get (ColumnDefinition.MDD));
                    resultModelSet.add (resultModel);

                    resultModel = new ResultModel ();
                    resultModel.setMetricType (MetricType.EQUITY_SERIES_IP);
                    resultModel.setDate (moment.getDate ());
                    resultModel.setValue (moment.get (ColumnDefinition.INSTANTPROFIT));
                    resultModelSet.add (resultModel);
                }
            }
        }
        summaryModel.setResults (resultModelSet);

        return summaryModel;
    }
}
