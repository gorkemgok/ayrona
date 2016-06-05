package com.ayronasystems.core.dao.mysql;

import java.util.List;

/**
 * Created by gorkemgok on 26/02/16.
 */
public class PaginatedResult<T> {

    private long count;

    private List<T> list;

    public PaginatedResult (long count, List<T> list) {
        this.count = count;
        this.list = list;
    }

    public long getCount () {
        return count;
    }

    public List<T> getList () {
        return list;
    }
}
