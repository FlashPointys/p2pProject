package com.flashpoint.p2p.service.loan;

import com.flashpoint.p2p.model.loan.IncomeRecord;
import com.flashpoint.p2p.model.vo.PaginationVO;

import java.util.List;
import java.util.Map;

public interface IncomeRecordService {
    /**
     * 通过用户的id查询收益信息
     * @param map
     * @return
     */
    List<IncomeRecord> queryIncomeRecordServiceByUid(Map<String, Object> map);

    /**
     * 分页查询最近收益
     * @param paramMap
     * @return
     */
    PaginationVO<IncomeRecord> queryIncomeRecordListByPage(Map<String, Object> paramMap);

    /**
     *生成收益计划开始
     */
    void generateIncomePlan();

    /**
     * 收益返还开始
     */
    void generateIncomeBack();
}
