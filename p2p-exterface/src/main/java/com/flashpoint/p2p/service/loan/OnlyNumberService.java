package com.flashpoint.p2p.service.loan;

public interface OnlyNumberService {
    /**
     * 生成redis全局唯一数
     * @return
     */
    Long getOnlyNumber();
}
