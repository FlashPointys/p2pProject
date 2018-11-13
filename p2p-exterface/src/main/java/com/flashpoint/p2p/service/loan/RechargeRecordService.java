package com.flashpoint.p2p.service.loan;

import com.flashpoint.p2p.model.loan.RechargeRecord;
import com.flashpoint.p2p.model.vo.PaginationVO;

import java.util.List;
import java.util.Map;

public interface RechargeRecordService {

    /**
     * 通过用户的id查询充值记录
     * @param map
     * @return
     */
    List<RechargeRecord> queryRechargeRecourdListByUid(Map<String, Object> map);

    /**
     * 通过用户id查询充值记录
     * @param paramMap
     * @return
     */
    PaginationVO<RechargeRecord> queryRechargeRecourdListByPage(Map<String, Object> paramMap);

    /**
     *添加一条充值记录
     * @param rechargeRecord
     * @return
     */
    int addRechargeRecord(RechargeRecord rechargeRecord);

    /**
     * 更新充值记录的状态为2
     * @param updateRechargeRecord
     * @return
     */
    int modifyRechargeRecordByRechargeNo(RechargeRecord updateRechargeRecord);

    /**
     * 用户充值
     * @param paramMap
     * @return
     */
    int recharge(Map<String, Object> paramMap);
}
