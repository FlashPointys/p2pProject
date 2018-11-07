package com.flashpoint.p2p.service.loan;

import com.flashpoint.p2p.model.loan.LoanInfo;
import com.flashpoint.p2p.model.vo.PaginationVO;

import java.util.List;
import java.util.Map;

public interface LoanInfoService {
    Double queryHistoryAverageRate();

    /**
     * 通过商品的类型查询商品
     * @param paraMap
     * @return
     */
    List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> paraMap);

    /**
     * 分页查询商品
     * @param map
     * @return
     */
    PaginationVO<LoanInfo> queryLoanInfoByPage(Map<String, Object> map);

    /**
     * 通过商品的id查询商品
     * @param id
     * @return
     */
    LoanInfo queryLoaninfoByLoanId(Integer id);
}
