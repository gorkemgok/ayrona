package com.ayronasystems.core.dao.mysql.model;

import com.ayronasystems.core.backtest.MetricType;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by gorkemgok on 10/01/16.
 */
@XmlRootElement
@Entity
@Table(name = "result", uniqueConstraints = @UniqueConstraint(columnNames = {"date", "metric_type", "summary_id"}))
public class ResultModel extends BaseModel {

    private MetricType metricType;

    private double value;

    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(name = "metric_type")
    public MetricType getMetricType () {
        return metricType;
    }

    public void setMetricType (MetricType metricType) {
        this.metricType = metricType;
    }

    public double getValue () {
        return value;
    }

    public void setValue (double value) {
        this.value = value;
    }

    public Date getDate () {
        return date;
    }

    public void setDate (Date date) {
        this.date = date;
    }

}
