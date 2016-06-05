package com.ayronasystems.core.backtest;

import java.util.Date;

/**
 * Created by gorkemgok on 10/01/16.
 */
public interface Summary {

    void setResult (MetricType metricType, MetricValue metricValue);

    void setResult (MetricType metricType, double value);

    void setResult (MetricType metricType, int value);

    MetricValue getResult (MetricType metricType);

    double getResultAsDouble (MetricType metricType);

    double getResultAsInteger (MetricType metricType);

    Date getStartDate ();

    Date getEndDate ();

    MetricType[] getSummarizedMetricTypes ();

}
