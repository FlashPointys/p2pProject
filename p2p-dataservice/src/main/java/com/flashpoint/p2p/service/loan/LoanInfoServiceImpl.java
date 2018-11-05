package com.flashpoint.p2p.service.loan;

import com.flashpoint.p2p.mapper.loan.LoanInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("loanInfoServiceImpl")
public class LoanInfoServiceImpl implements  LoanInfoService{

    @Autowired
    private LoanInfoMapper loanInfoMapper;
    @Override
    public Double queryHistoryAverageRate() {

        return loanInfoMapper.selectHistoryAverateRate();
    }
}
