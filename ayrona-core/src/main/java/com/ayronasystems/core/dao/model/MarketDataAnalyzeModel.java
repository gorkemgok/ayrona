package com.ayronasystems.core.dao.model;

import org.mongodb.morphia.annotations.Entity;

import java.util.List;

/**
 * Created by gorkemgok on 05/06/16.
 */
@Entity("md_analyze")
public class MarketDataAnalyzeModel extends BaseModel{

    private int totalAbsentBar;

    private List<AbsentBar> absentBarList;

    public int getTotalAbsentBar () {
        return totalAbsentBar;
    }

    public void setTotalAbsentBar (int totalAbsentBar) {
        this.totalAbsentBar = totalAbsentBar;
    }

    public List<AbsentBar> getAbsentBarList () {
        return absentBarList;
    }

    public void setAbsentBarList (List<AbsentBar> absentBarList) {
        this.absentBarList = absentBarList;
    }

}
