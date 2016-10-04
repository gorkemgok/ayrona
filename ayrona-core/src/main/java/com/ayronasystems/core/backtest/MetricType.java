package com.ayronasystems.core.backtest;

import com.ayronasystems.core.timeseries.series.SymbolTimeSeries;

/**
 * Created by gorkemgok on 06/01/16.
 */
public enum MetricType {
    NET_PROFIT(Double.TYPE, "np"),
    GROSS_PROFIT(Double.TYPE, "gp"),
    GROSS_LOSS(Double.TYPE, "gl"),
    NET_PROFIT_PERCENTAGE(Double.TYPE, "np_p"),
    GROSS_PROFIT_PERCENTAGE(Double.TYPE, "gp_p"),
    GROSS_LOSS_PERCENTAGE(Double.TYPE, "gl_p"),
    PROFIT_FACTOR(Double.TYPE, "pf"),
    PROFIT_STD(Double.TYPE, "pstd"),
    NEGATIVE_PROFIT_STD(Double.TYPE, "npstd"),

    TOTAL_NUMBER_OF_TRADES(Integer.TYPE, "tt"),
    WINNING_TRADE_COUNT(Integer.TYPE, "wt"),
    LOSING_TRADE_COUNT(Integer.TYPE, "lt"),
    PROFITABLE_PERCENT(Double.TYPE, "pp"),

    AVE_TRADE_NET_PROFIT(Double.TYPE, "anp"),
    AVE_TRADE_NET_PROFIT_PERCENTAGE(Double.TYPE, "anp_p"),
    AVE_WINNING_TRADE(Double.TYPE, "awt"),
    AVE_WINNING_TRADE_PERCENTAGE(Double.TYPE, "awt_p"),
    AVE_LOSING_TRADE(Double.TYPE, "alt"),
    AVE_LOSING_TRADE_PERCENTAGE(Double.TYPE, "alt_p"),
    RATIO_AVE_WINNING_LOSING(Double.TYPE, "awl_r"),
    LARGEST_WINNING_TRADE(Double.TYPE, "lwt"),
    LARGEST_LOSING_TRADE(Double.TYPE, "llt"),

    MAX_CONSECUTIVE_WINNING_TRADES(Integer.TYPE, "cwt"),
    MAX_CONSECUTIVE_LOSING_TRADES(Integer.TYPE, "clt"),
    AVE_BARS_IN_WINNING_TRADES(Double.TYPE, "awb"),
    AVE_BARS_IN_LOSING_TRADES(Double.TYPE, "alb"),

    MAX_TRADE_DRAWDOWN(Double.TYPE, "mdd"),
    MAX_TRADE_DRAWDOWN_PERCENTAGE(Double.TYPE, "mdd_p"),
    STABILITY(Double.TYPE, "stb"),

    DAILY_STD(Double.TYPE, "dstd"),
    DAILY_RSQUARED(Double.TYPE, "dstb"),
    WEEKLY_STD(Double.TYPE, "wstd"),
    WEEKLY_RSQUARED(Double.TYPE, "wstb"),
    MONTHLY_STD(Double.TYPE, "mstd"),
    MONTHLY_RSQUARED(Double.TYPE, "mstb"),
    YEARLY_STD(Double.TYPE, "ystd"),
    YEARLY_RSQUARED(Double.TYPE, "ystb"),

    SHARPE(Double.TYPE, "sharpe"),
    SORTINO(Double.TYPE, "sortino"),

    EQUITY_SERIES (SymbolTimeSeries.class, "eqs"),
    EQUITY_SERIES_EQUITY(Double.TYPE, "eqsq"),
    EQUITY_SERIES_MDD(Double.TYPE, "eqsmdd"),
    EQUITY_SERIES_IP(Double.TYPE, "eqsip"),
    ;

    private Class valueType;

    private String equationSymbol;

    MetricType (Class valueType, String equationSymbol) {
        this.valueType = valueType;
        this.equationSymbol = equationSymbol;
    }

    public String getEquationSymbol () {
        return equationSymbol;
    }

    public Class getValueType () {
        return valueType;
    }
}
