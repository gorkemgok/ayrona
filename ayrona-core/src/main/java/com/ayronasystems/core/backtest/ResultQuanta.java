package com.ayronasystems.core.backtest;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gorkemgok on 29/08/16.
 */
public class ResultQuanta {

    private Date date;

    private Map<ResultQuantaMetric, Double> valueList = new HashMap<ResultQuantaMetric, Double> ();

    public ResultQuanta (Date date, Map<ResultQuantaMetric, Double> valueList) {
        this.date = date;
        this.valueList = valueList;
    }

    public Date getDate () {
        return date;
    }

    public double getValue(ResultQuantaMetric resultQuantaMetric){
        return valueList.get (resultQuantaMetric);
    }

    @Override
    public String toString () {
        DecimalFormat df = new DecimalFormat ("##.####");
        StringBuilder sb = new StringBuilder ("ResultQuanta: ");
        for(Map.Entry<ResultQuantaMetric, Double> entry : valueList.entrySet ()){
            sb.append (entry.getKey ())
                    .append (":")
                    .append (df.format (entry.getValue ())).append (", ");
        }
        return sb.toString ();
    }
}
