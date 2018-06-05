package com.ctrip.lpxie.basement.knight.servlet;

import java.io.Serializable;

/**
 * Created by lpxie on 2016/7/22.
 */
public class FilterDef implements Serializable {
    private static final long serialVersionUID = 1L;

    private String filterName = null;

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    private String filterClass = null;

    public String getFilterClass() {
        return filterClass;
    }

    public void setFilterClass(String filterClass) {
        this.filterClass = filterClass;
    }

    private Filter filter = null;

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}
