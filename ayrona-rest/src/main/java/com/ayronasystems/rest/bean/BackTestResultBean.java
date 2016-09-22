package com.ayronasystems.rest.bean;

import com.ayronasystems.core.backtest.BackTestResult;
import com.ayronasystems.core.backtest.MetricType;
import com.ayronasystems.core.backtest.ResultPeriod;
import com.ayronasystems.core.strategy.Position;
import com.ayronasystems.core.timeseries.moment.EquityBar;
import com.ayronasystems.core.timeseries.series.BasicTimeSeries;
import org.joda.time.DateTime;

import java.util.*;

/**
 * Created by gorkemgok on 20/06/16.
 */
public class BackTestResultBean {

    public static class SeriesBean{

        private ResultPeriod period;

        private List<Date> dateSeries;

        private List<Double> equitySeries;

        private List<Double> mddSeries;

        private List<Double> profitSeries;

        public ResultPeriod getPeriod () {
            return period;
        }

        public void setPeriod (ResultPeriod period) {
            this.period = period;
        }

        public List<Date> getDateSeries () {
            return dateSeries;
        }

        public void setDateSeries (List<Date> dateSeries) {
            this.dateSeries = dateSeries;
        }

        public List<Double> getEquitySeries () {
            return equitySeries;
        }

        public void setEquitySeries (List<Double> equitySeries) {
            this.equitySeries = equitySeries;
        }

        public List<Double> getMddSeries () {
            return mddSeries;
        }

        public void setMddSeries (List<Double> mddSeries) {
            this.mddSeries = mddSeries;
        }

        public List<Double> getProfitSeries () {
            return profitSeries;
        }

        public void setProfitSeries (List<Double> profitSeries) {
            this.profitSeries = profitSeries;
        }
    }

    private Map<MetricType, Double> results;

    private List<SeriesBean> series;

    private List<Position> positionList;

    public Map<MetricType, Double> getResults () {
        return results;
    }

    public void setResults (Map<MetricType, Double> results) {
        this.results = results;
    }

    public List<Position> getPositionList () {
        return positionList;
    }

    public void setPositionList (List<Position> positionList) {
        this.positionList = positionList;
    }

    public List<SeriesBean> getSeries () {
        return series;
    }

    public void setSeries (List<SeriesBean> series) {
        this.series = series;
    }

    public static BackTestResultBean valueOf(BackTestResult btr){
        Map<MetricType, Double> results = new TreeMap<MetricType, Double> ();
        for (MetricType metricType : btr.getSummarizedMetricTypes ()){
            if (metricType.getValueType ().equals (Double.TYPE))
                results.put (metricType, btr.getResultAsDouble (metricType));
            else if (metricType.getValueType ().equals (Integer.TYPE))
                results.put (metricType, btr.getResultAsInteger (metricType));
        }

        BasicTimeSeries<EquityBar> equityTimeSeries = (BasicTimeSeries<EquityBar>)
                btr.getResult (MetricType.EQUITY_SERIES).getValue ();
        List<Date> dateSeries = new ArrayList<Date>();
        List<Double> equitySeries = new ArrayList<Double>();
        List<Double> profitSeries = new ArrayList<Double>();
        List<Double> mddSeries = new ArrayList<Double>();
        int i = 0;
        Date lastDate = null;
        double ip = 0;
        double mdd = Double.MAX_VALUE;
        double equity = 0;
        for (EquityBar equityBar : equityTimeSeries){
            DateTime dateTime = new DateTime(equityBar.getDate());
            Date currentDate = dateTime.withMillisOfDay(0).toDate();
            if (lastDate != null && !currentDate.equals(lastDate)){
                dateSeries.add(lastDate);
                equitySeries.add(equity);
                profitSeries.add(ip);
                mddSeries.add(mdd);
                ip = 0;
                mdd = Double.MAX_VALUE;
            }
            equity = equityBar.getEquity ();
            ip += equityBar.getInstantProfit ();
            mdd = Math.min(equityBar.getMdd (), mdd);
            lastDate = currentDate;
        }
        SeriesBean seriesBean = new SeriesBean ();
        seriesBean.setPeriod (ResultPeriod.DAY);
        seriesBean.setDateSeries (dateSeries);
        seriesBean.setEquitySeries (equitySeries);
        seriesBean.setProfitSeries (profitSeries);
        seriesBean.setMddSeries (mddSeries);

        BackTestResultBean btrBean = new BackTestResultBean ();
        btrBean.setPositionList (btr.getPositionList ());
        btrBean.setResults (results);
        btrBean.setSeries (Arrays.asList (new SeriesBean[]{seriesBean}));
        return btrBean;
    }
}
