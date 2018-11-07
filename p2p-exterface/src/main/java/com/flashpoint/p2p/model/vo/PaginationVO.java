package com.flashpoint.p2p.model.vo;

import java.io.Serializable;
import java.util.List;

public class PaginationVO<T> implements Serializable {

    /**
     * 总条数
     */
    private Long total;
    /**
     * 存储的数据
     */
    private List<T> dataList;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
