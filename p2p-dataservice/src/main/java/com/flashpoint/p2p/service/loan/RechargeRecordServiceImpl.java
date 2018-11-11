package com.flashpoint.p2p.service.loan;

import com.flashpoint.p2p.mapper.loan.RechargeRecordMapper;
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
}
