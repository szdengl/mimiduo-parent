package net.mimiduo.boot.util;

import java.util.List;

public class BootstrapUIPage<T> {
    private long total;
    private List<T> rows;

    public BootstrapUIPage() {
    }

    public BootstrapUIPage(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
