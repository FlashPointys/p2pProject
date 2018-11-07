package com.flashpoint.p2p.service.loan;

import com.flashpoint.p2p.common.Constant;
import com.flashpoint.p2p.mapper.loan.LoanInfoMapper;
import com.flashpoint.p2p.model.loan.LoanInfo;
import com.flashpoint.p2p.model.vo.PaginationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service("loanInfoServiceImpl")
public class LoanInfoServiceImpl implements  LoanInfoService{

    @Autowired
    private LoanInfoMapper loanInfoMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Override
    public Double queryHistoryAverageRate() {
        //首先去redis缓存中查询，有：直接 ，没有：去数据库查询，然后并存放到redis缓存中

        //去redis中获取该值
        Double historyAverageRate= (Double) redisTemplate.opsForValue().get(Constant.HISTORY_AVERAGE_RATE);
        if(historyAverageRate==null){
            historyAverageRate= loanInfoMapper.selectHistoryAverateRate();
            redisTemplate.opsForValue().set(Constant.HISTORY_AVERAGE_RATE,historyAverageRate,15, TimeUnit.SECONDS);

        }
        return historyAverageRate;


    }

    @Override
    public List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> paraMap) {
        return loanInfoMapper.queryLoanInfoByPage(paraMap);
    }

    @Override
    public PaginationVO<LoanInfo> queryLoanInfoByPage(Map<String, Object> map) {
        PaginationVO<LoanInfo> paginationVO=new PaginationVO<>();
        paginationVO.setTotal(loanInfoMapper.selectTotal(map));

        paginationVO.setDataList(loanInfoMapper.selectLoanInfoByPage(map));
        return paginationVO;
    }

    @Override
    public LoanInfo queryLoaninfoByLoanId(Integer id) {

        return loanInfoMapper.selectByPrimaryKey(id);
    }
}
