package com.ayronasystems.rest.bean;

import com.ayronasystems.core.strategy.Position;
import com.ayronasystems.core.backtest.BackTestResult;
import com.ayronasystems.core.backtest.MetricType;
import com.ayronasystems.core.timeseries.moment.EquityBar;
import com.ayronasystems.core.timeseries.series.BasicTimeSeries;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 20/06/16.
 */
public class BackTestResultBean {

    private double netProfit;

    private double netProfitPercentage;

    private double mdd;

    private double stability;

    private double profitStd;

    private double sharpe;

    private double sortino;

    private List<Date> dateSeries;

    private List<Double> equitySeries;

    private List<Double> mddSeries;

    private List<Double> profitSeries;

    private List<Position> positionList;

    public List<Position> getPositionList () {
        return positionList;
    }

    public void setPositionList (List<Position> positionList) {
        this.positionList = positionList;
    }

    public double getNetProfit () {
        return netProfit;
    }

    public void setNetProfit (double netProfit) {
        this.netProfit = netProfit;
    }

    public double getMdd () {
        return mdd;
    }

    public void setMdd (double mdd) {
        this.mdd = mdd;
    }

    public double getStability () {
        return stability;
    }

    public void setStability (double stability) {
        this.stability = stability;
    }

    public List<Date> getDateSeries() {
        return dateSeries;
    }

    public void setDateSeries(List<Date> dateSeries) {
        this.dateSeries = dateSeries;
    }

    public List<Double> getEquitySeries() {
        return equitySeries;
    }

    public void setEquitySeries(List<Double> equitySeries) {
        this.equitySeries = equitySeries;
    }

    public List<Double> getMddSeries() {
        return mddSeries;
    }

    public void setMddSeries(List<Double> mddSeries) {
        this.mddSeries = mddSeries;
    }

    public List<Double> getProfitSeries() {
        return profitSeries;
    }

    public void setProfitSeries(List<Double> profitSeries) {
        this.profitSeries = profitSeries;
    }

    public double getNetProfitPercentage () {
        return netProfitPercentage;
    }

    public void setNetProfitPercentage (double netProfitPercentage) {
        this.netProfitPercentage = netProfitPercentage;
    }

    public double getProfitStd () {
        return profitStd;
    }

    public void setProfitStd (double profitStd) {
        this.profitStd = profitStd;
    }

    public double getSharpe () {
        return sharpe;
    }

    public void setSharpe (double sharpe) {
        this.sharpe = sharpe;
    }

    public double getSortino () {
        return sortino;
    }

    public void setSortino (double sortino) {
        this.sortino = sortino;
    }

    public static BackTestResultBean valueOf(BackTestResult btr){
        BackTestResultBean btrBean = new BackTestResultBean ();
        btrBean.setNetProfit (btr.getResultAsDouble (MetricType.NET_PROFIT));
        btrBean.setNetProfitPercentage (btr.getResultAsDouble (MetricType.NET_PROFIT_PERCENTAGE));
        btrBean.setMdd (btr.getResultAsDouble (MetricType.MAX_TRADE_DRAWDOWN));
        btrBean.setStability (btr.getResultAsDouble (MetricType.STABILITY));
        btrBean.setProfitStd (btr.getResultAsDouble (MetricType.PROFIT_STD));
        btrBean.setSharpe (btr.getResultAsDouble (MetricType.SHARPE));
        btrBean.setSortino (btr.getResultAsDouble (MetricType.SORTINO));
        btrBean.setPositionList (btr.getPositionList ());
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
        btrBean.setDateSeries(dateSeries);
        btrBean.setEquitySeries (equitySeries);
        btrBean.setProfitSeries (profitSeries);
        btrBean.setMddSeries (mddSeries);
        return btrBean;
    }
}
