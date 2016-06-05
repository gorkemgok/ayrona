package com.ayronasystems.core.dao.mysql;

/**
 * Created by gorkemgok on 26/02/16.
 */
public class LimitOffset {

    private int page;

    private int itemCount;

    public LimitOffset (int page, int itemCount) {
        this.page = page;
        this.itemCount = itemCount;
    }

    public int getLimit(){
        return itemCount;
    }

    public int getOffset(){
        return itemCount * (page - 1);
    }

    public int getPage () {
        return page;
    }

    public int getItemCount () {
        return itemCount;
    }
}
