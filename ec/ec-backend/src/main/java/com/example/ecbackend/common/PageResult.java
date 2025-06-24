package com.example.ecbackend.common;

import java.util.List;

public class PageResult<T> {
    private List<T> records;
    private int total;

    public PageResult(List<T> records, int total) {
        this.records = records;
        this.total = total;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
