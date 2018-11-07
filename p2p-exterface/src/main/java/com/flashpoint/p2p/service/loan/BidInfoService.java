package com.flashpoint.p2p.service.loan;


import com.flashpoint.p2p.model.loan.BidInfo;

import java.util.List;

public interface BidInfoService {
    /**
     * 查找平台总投资金额
     * @return
     */
    Double queryBidAllMoney();

    /**
     * 通过商品id查询投资记录
     * @param id
     * @return
     */
    List<BidInfo> queryBidInfoListByLoanId(Integer id);
}
