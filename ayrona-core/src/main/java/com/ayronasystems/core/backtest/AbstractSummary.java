package com.ayronasystems.core.backtest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gorkemgok on 10/01/16.
 */
public abstract class AbstractSummary implements Summary{

    private Date startDate;

    private Date endDate;

    private Map<MetricType, MetricValue> results = new HashMap<MetricType, MetricValue> ();

    public AbstractSummary (Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setResult (MetricType metricType, MetricValue metricValue) {
        if ( results.containsKey (metricType) ) {
            results.get (metricType)
                   .setValue (metricValue.getValue ());
        } else{
            results.put (metricType, metricValue);
        }
    }

    public void setResult (MetricType metricType, double value) {
        setResult (metricType, new MetricValue<Double> (value));
    }

    public void setResult (MetricType metricType, int value) {
        setResult (metricType, new MetricValue<Integer> (value));
    }

    public MetricValue getResult(MetricType metricType){
        if (results.containsKey (metricType)){
            return results.get (metricType);
        }
        return new MetricValue (null);
    }

    public double getResultAsDouble(MetricType metricType){
        Object obj = getResult (metricType).getValue ();
        if (obj != null) {
            return (Double) obj;
        } else {
            return Double.NaN;
        }
    }

    public double getResultAsInteger(MetricType metricType){
        Object obj = getResult (metricType).getValue ();
        if (obj != null) {
            return (Integer) obj;
        } else {
            return Double.NaN;
        }
    }

    public Date getStartDate () {
        return startDate;
    }

    public Date getEndDate () {
        return endDate;
    }
}
