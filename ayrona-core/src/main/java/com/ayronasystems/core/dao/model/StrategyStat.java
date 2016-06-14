package com.ayronasystems.core.dao.model;

/**
 * Created by gorkemgok on 14/06/16.
 */
public class StrategyStat {

    private long totalCount;

    private long activeCount;

    private long inactiveCount;

    public StrategyStat (long activeCount, long inactiveCount) {
        this.activeCount = activeCount;
        this.inactiveCount = inactiveCount;
        this.totalCount = activeCount + inactiveCount;
    }

    public long getTotalCount () {
        return totalCount;
    }

    public long getActiveCount () {
        return activeCount;
    }

    public long getInactiveCount () {
        return inactiveCount;
    }
}
