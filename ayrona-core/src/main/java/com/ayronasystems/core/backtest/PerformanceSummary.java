package com.ayronasystems.core.backtest;

import com.ayronasystems.core.util.DateUtils;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gorkemgok on 06/01/16.
 */
public class PerformanceSummary extends AbstractSummary{

    public static final String SPACE = "%30s";

    public static final MetricType[] METRIC_TYPES = new MetricType[]{
            MetricType.NET_PROFIT,
            MetricType.GROSS_PROFIT,
            MetricType.GROSS_LOSS,
            MetricType.PROFIT_FACTOR,

            MetricType.TOTAL_NUMBER_OF_TRADES,
            MetricType.WINNING_TRADE_COUNT,
            MetricType.LOSING_TRADE_COUNT,
            MetricType.PROFITABLE_PERCENT,

            MetricType.AVE_TRADE_NET_PROFIT,
            MetricType.AVE_WINNING_TRADE,
            MetricType.AVE_LOSING_TRADE,
            MetricType.RATIO_AVE_WINNING_LOSING,
            MetricType.LARGEST_WINNING_TRADE,
            MetricType.LARGEST_LOSING_TRADE,

            MetricType.MAX_CONSECUTIVE_WINNING_TRADES,
            MetricType.MAX_CONSECUTIVE_LOSING_TRADES,
            MetricType.AVE_BARS_IN_WINNING_TRADES,
            MetricType.AVE_BARS_IN_LOSING_TRADES,

            MetricType.MAX_TRADE_DRAWDOWN
    };

    private Map<MetricType, MetricValue> results = new HashMap<MetricType, MetricValue> ();

    public PerformanceSummary (Date startDate, Date endDate) {
        super (startDate, endDate);
    }

    @Override
    public String toString () {
        StringBuilder stringBuilder = new StringBuilder ("Summary").append ("\n");
        stringBuilder.append (String.format ("%30s", "Start Date"))
                     .append ("\t:")
                     .append (DateUtils.formatDate (getStartDate ()))
                     .append ("\n");
        stringBuilder.append (String.format ("%30s", "End Date")).append ("\t:")
                     .append (DateUtils.formatDate (getEndDate ()))
                     .append ("\n");
        for (MetricType metricType : getSummarizedMetricTypes ()){
            MetricValue value = getResult (metricType);

            if (value != null){
                Object v = value.getValue ();
                stringBuilder.append (String.format (SPACE, metricType.name ())).append ("\t:");
                if ( v instanceof Double){
                    stringBuilder.append (new DecimalFormat ("#0.0000").format (v)).append ("\n");
                }else if ( v instanceof Integer){
                    stringBuilder.append (v).append ("\n");
                }
            }
        }
        return stringBuilder.toString ();
    }

    public MetricType[] getSummarizedMetricTypes () {
        return METRIC_TYPES;
    }
}
