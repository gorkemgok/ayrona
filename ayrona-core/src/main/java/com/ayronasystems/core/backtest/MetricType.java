package com.ayronasystems.core.backtest;

import com.ayronasystems.core.timeseries.series.SymbolTimeSeries;

/**
 * Created by gorkemgok on 06/01/16.
 */
public enum MetricType {
    NET_PROFIT(Double.TYPE),
    GROSS_PROFIT(Double.TYPE),
    GROSS_LOSS(Double.TYPE),
    NET_PROFIT_PERCENTAGE(Double.TYPE),
    GROSS_PROFIT_PERCENTAGE(Double.TYPE),
    GROSS_LOSS_PERCENTAGE(Double.TYPE),
    PROFIT_FACTOR(Double.TYPE),
    PROFIT_STD(Double.TYPE),
    NEGATIVE_PROFIT_STD(Double.TYPE),

    TOTAL_NUMBER_OF_TRADES(Integer.TYPE),
    WINNING_TRADE_COUNT(Integer.TYPE),
    LOSING_TRADE_COUNT(Integer.TYPE),
    PROFITABLE_PERCENT(Double.TYPE),

    AVE_TRADE_NET_PROFIT(Double.TYPE),
    AVE_TRADE_NET_PROFIT_PERCENTAGE(Double.TYPE),
    AVE_WINNING_TRADE(Double.TYPE),
    AVE_WINNING_TRADE_PERCENTAGE(Double.TYPE),
    AVE_LOSING_TRADE(Double.TYPE),
    AVE_LOSING_TRADE_PERCENTAGE(Double.TYPE),
    RATIO_AVE_WINNING_LOSING(Double.TYPE),
    LARGEST_WINNING_TRADE(Double.TYPE),
    LARGEST_LOSING_TRADE(Double.TYPE),

    MAX_CONSECUTIVE_WINNING_TRADES(Integer.TYPE),
    MAX_CONSECUTIVE_LOSING_TRADES(Integer.TYPE),
    AVE_BARS_IN_WINNING_TRADES(Double.TYPE),
    AVE_BARS_IN_LOSING_TRADES(Double.TYPE),

    MAX_TRADE_DRAWDOWN(Double.TYPE),
    MAX_TRADE_DRAWDOWN_PERCENTAGE(Double.TYPE),
    STABILITY(Double.TYPE),

    DAILY_STD(Double.TYPE),
    DAILY_RSQUARED(Double.TYPE),
    WEEKLY_STD(Double.TYPE),
    WEEKLY_RSQUARED(Double.TYPE),
    MONTHLY_STD(Double.TYPE),
    MONTHLY_RSQUARED(Double.TYPE),
    YEARLY_STD(Double.TYPE),
    YEARLY_RSQUARED(Double.TYPE),

    SHARPE(Double.TYPE),
    SORTINO(Double.TYPE),

    EQUITY_SERIES (SymbolTimeSeries.class),
    EQUITY_SERIES_EQUITY(Double.TYPE),
    EQUITY_SERIES_MDD(Double.TYPE),
    EQUITY_SERIES_IP(Double.TYPE),
    ;

    private Class valueType;

    MetricType (Class valueType) {
        this.valueType = valueType;
    }

    public Class getValueType () {
        return valueType;
    }
}
