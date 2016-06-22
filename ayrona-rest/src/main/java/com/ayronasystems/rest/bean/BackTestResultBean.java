package com.ayronasystems.rest.bean;

import com.ayronasystems.core.backtest.BackTestResult;
import com.ayronasystems.core.backtest.MetricType;
import com.ayronasystems.core.timeseries.moment.EquityBar;
import com.ayronasystems.core.timeseries.series.BasicTimeSeries;

/**
 * Created by gorkemgok on 20/06/16.
 */
public class BackTestResultBean {

    private double netProfit;

    private double mdd;

    private double stability;

    private double[] equitySeries;

    private double[] mddSeries;

    private double[] profitSeries;

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

    public double[] getMddSeries () {
        return mddSeries;
    }

    public void setMddSeries (double[] mddSeries) {
        this.mddSeries = mddSeries;
    }

    public double[] getEquitySeries () {
        return equitySeries;
    }

    public void setEquitySeries (double[] equitySeries) {
        this.equitySeries = equitySeries;
    }

    public double[] getProfitSeries () {
        return profitSeries;
    }

    public void setProfitSeries (double[] profitSeries) {
        this.profitSeries = profitSeries;
    }

    public static BackTestResultBean valueOf(BackTestResult btr){
        BackTestResultBean btrBean = new BackTestResultBean ();
        btrBean.setNetProfit (btr.getResultAsDouble (MetricType.NET_PROFIT));
        btrBean.setMdd (btr.getResultAsDouble (MetricType.MAX_TRADE_DRAWDOWN));
        btrBean.setStability (btr.getResultAsDouble (MetricType.STABILITY));

        BasicTimeSeries<EquityBar> equityTimeSeries = (BasicTimeSeries<EquityBar>)
                btr.getResult (MetricType.EQUITY_SERIES).getValue ();
        int size = equityTimeSeries.size ();
        double[] equitySeries = new double[size];
        double[] profitSeries = new double[size];
        double[] mddSeries = new double[size];
        int i = 0;
        for (EquityBar equityBar : equityTimeSeries){
            equitySeries[i] = equityBar.getEquity ();
            profitSeries[i] = equityBar.getInstantProfit ();
            mddSeries[i] = equityBar.getMdd ();
        }
        btrBean.setEquitySeries (equitySeries);
        btrBean.setProfitSeries (profitSeries);
        btrBean.setMddSeries (mddSeries);
        return btrBean;
    }
}
