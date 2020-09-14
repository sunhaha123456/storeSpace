package com.store.common.data.base;

import java.util.List;

@lombok.Data
public class PageList<T> {

    private long total;

    private List<T> rows;

    private Object otherData;

    public PageList() {

    }

    public PageList(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }
}