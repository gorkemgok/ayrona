package com.ayronasystems.core.backtest;

import com.ayronasystems.core.util.DateUtils;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by gorkemgok on 21/05/16.
 */
public class BackTestResult extends AbstractSummary {

    public static final String SPACE = "%30s";

    public BackTestResult (Date startDate, Date endDate) {
        super (startDate, endDate);
    }

    public MetricType[] getSummarizedMetricTypes () {
        return MetricType.values ();
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
}
