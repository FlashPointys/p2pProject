package com.flashpoint.p2p.service.loan;


import com.flashpoint.p2p.model.loan.BidInfo;
import com.flashpoint.p2p.model.vo.BidUserTop;
import com.flashpoint.p2p.model.vo.PaginationVO;
import com.flashpoint.p2p.model.vo.ResultObject;

import java.util.List;
import java.util.Map;

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

    /**
     * 根据用户id查询交易记录
     * @param map
     * @return
     */
    List<BidInfo> queryBidinfoListByUid(Map<String, Object> map);

    /**
     * 分页查询交易记录
     * @param paramMap
     * @return
     */
    PaginationVO<BidInfo> queryBidinfoListByPage(Map<String, Object> paramMap);

    /**
     * 用户对商品进行投资
     * @param retMap
     * @return
     */
    ResultObject invest(Map<String, Object> retMap);

    /**
     * 查询用户的投资排行榜
     * @return
     */
    List<BidUserTop> queryBidUserTop();
}
