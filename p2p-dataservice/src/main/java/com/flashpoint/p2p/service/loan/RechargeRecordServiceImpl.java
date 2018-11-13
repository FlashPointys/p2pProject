package com.flashpoint.p2p.service.loan;

import com.flashpoint.p2p.mapper.loan.RechargeRecordMapper;
import com.flashpoint.p2p.mapper.user.FinanceAccountMapper;
import com.flashpoint.p2p.model.loan.RechargeRecord;
import com.flashpoint.p2p.model.vo.PaginationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RechargeRecordServiceImpl implements  RechargeRecordService {
    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;
    @Override
    public List<RechargeRecord> queryRechargeRecourdListByUid(Map<String, Object> map) {

        return rechargeRecordMapper.selectRecharageRecourdListByPage(map);
    }

    @Override
    public PaginationVO<RechargeRecord> queryRechargeRecourdListByPage(Map<String, Object> paramMap) {
        PaginationVO<RechargeRecord> paginationVO=new PaginationVO<>();
        paginationVO.setTotal(rechargeRecordMapper.selectRecharageRecourdCountByUid(paramMap));
        paginationVO.setDataList(rechargeRecordMapper.selectRecharageRecourdListByPage(paramMap));
        return paginationVO;
    }

    @Override
    public int addRechargeRecord(RechargeRecord rechargeRecord) {
        return rechargeRecordMapper.insertSelective(rechargeRecord);
    }

    @Override
    public int modifyRechargeRecordByRechargeNo(RechargeRecord updateRechargeRecord) {
        return rechargeRecordMapper.updateRechargeRecordByRechargeNo(updateRechargeRecord);

    }

    @Override
    public int recharge(Map<String, Object> paramMap) {
        //更新帐户可用余额
        int updateFinanceAccountCount = financeAccountMapper.updateFinanceAccountByRecharge(paramMap);

        if (updateFinanceAccountCount > 0) {
            //更新充值记录的状态
            RechargeRecord rechargeRecord = new RechargeRecord();
            rechargeRecord.setRechargeNo((String) paramMap.get("rechargeNo"));
            rechargeRecord.setRechargeStatus((String) paramMap.get("rechargeStatus"));
            int updateRechargeCount = rechargeRecordMapper.updateRechargeRecordByRechargeNo(rechargeRecord);

            if (updateRechargeCount <= 0) {
                return 0;
            }

        } else {
            return 0;
        }


        return 1;
    }
}
