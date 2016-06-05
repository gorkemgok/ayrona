package com.ayronasystems.core.dao.model;

import org.mongodb.morphia.annotations.Embedded;

import java.util.Date;

/**
 * Created by gorkemgok on 05/06/16.
 */
@Embedded
public class AbsentBar {

    private Date startDate;

    private Date endDate;

    private int absentBarCount;

    public Date getStartDate () {
        return startDate;
    }

    public void setStartDate (Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate () {
        return endDate;
    }

    public void setEndDate (Date endDate) {
        this.endDate = endDate;
    }

    public int getAbsentBarCount () {
        return absentBarCount;
    }

    public void setAbsentBarCount (int absentBarCount) {
        this.absentBarCount = absentBarCount;
    }
}
