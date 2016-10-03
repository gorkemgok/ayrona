package com.ayronasystems.rest.bean;

import com.ayronasystems.core.backtest.*;
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

        private List<Date> dateList;

        private Map<ResultQuantaMetric, List<Double>> map = new TreeMap<ResultQuantaMetric, List<Double>> ();

        public Map<ResultQuantaMetric, List<Double>> getMap () {
            return map;
        }

        public void setMap (Map<ResultQuantaMetric, List<Double>> map) {
            this.map = map;
        }

        public void addToSeries(ResultQuantaMetric metric, Double value){
            List<Double> series = this.map.get (metric);
            if (series != null){
                series.add (value);
            }else{
                List<Double> newSeries = new ArrayList<Double> ();
                newSeries.add (value);
                this.map.put (metric, newSeries);
            }
        }

        public ResultPeriod getPeriod () {
            return period;
        }

        public void setPeriod (ResultPeriod period) {
            this.period = period;
        }

        public List<Date> getDateList () {
            return dateList;
        }

        public void setDateList (List<Date> dateList) {
            this.dateList = dateList;
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
        List<SeriesBean> seriesBeanList = new ArrayList<SeriesBean> ();
        BasicTimeSeries<EquityBar> equityTimeSeries = (BasicTimeSeries<EquityBar>)
                btr.getResult (MetricType.EQUITY_SERIES).getValue ();
        if (equityTimeSeries != null) {
            List<Date> dateSeries = new ArrayList<Date> ();
            SeriesBean seriesBean = new SeriesBean ();
            Date lastDate = null;
            double ip = 0;
            double mdd = Double.MAX_VALUE;
            double equity = 0;
            for ( EquityBar equityBar : equityTimeSeries ) {
                DateTime dateTime = new DateTime (equityBar.getDate ());
                Date currentDate = dateTime.withMillisOfDay (0)
                                           .toDate ();
                if ( lastDate != null && !currentDate.equals (lastDate) ) {
                    dateSeries.add (lastDate);
                    seriesBean.addToSeries (ResultQuantaMetric.EQUITY, equity);
                    seriesBean.addToSeries (ResultQuantaMetric.NET_PROFIT, ip);
                    seriesBean.addToSeries (ResultQuantaMetric.MDD, mdd);
                    ip = 0;
                    mdd = Double.MAX_VALUE;
                }
                equity = equityBar.getEquity ();
                ip += equityBar.getInstantProfit ();
                mdd = Math.min (equityBar.getMdd (), mdd);
                lastDate = currentDate;
            }

            seriesBean.setPeriod (ResultPeriod.DAY);
            seriesBean.setDateList (dateSeries);

            seriesBeanList.add (seriesBean);
        }else{
            for (Map.Entry<ResultPeriod, List<ResultQuanta>> entry: btr.getPeriodicResultMap ().entrySet ()){
                SeriesBean seriesBean = new SeriesBean ();
                seriesBean.setPeriod (entry.getKey ());

                List<Date> dateSeries = new ArrayList<Date> ();
                for (ResultQuanta resultQuanta : entry.getValue ()){
                    dateSeries.add (resultQuanta.getDate ());
                    seriesBean.addToSeries (ResultQuantaMetric.NET_PROFIT, resultQuanta.getValue (ResultQuantaMetric.NET_PROFIT));
                    seriesBean.addToSeries (ResultQuantaMetric.EQUITY, resultQuanta.getValue (ResultQuantaMetric.EQUITY));
                    seriesBean.addToSeries (ResultQuantaMetric.MDD, resultQuanta.getValue (ResultQuantaMetric.MDD));
                }

                seriesBean.setDateList (dateSeries);
                seriesBeanList.add (seriesBean);
            }
        }

        BackTestResultBean btrBean = new BackTestResultBean ();
        btrBean.setPositionList (btr.getPositionList ());
        btrBean.setResults (results);
        btrBean.setSeries (seriesBeanList);
        return btrBean;
    }
}
