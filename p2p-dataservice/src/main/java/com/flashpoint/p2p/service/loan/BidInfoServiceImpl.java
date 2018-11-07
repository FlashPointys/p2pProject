package com.flashpoint.p2p.service.loan;

import com.flashpoint.p2p.common.Constant;
import com.flashpoint.p2p.mapper.loan.BidInfoMapper;
import com.flashpoint.p2p.model.loan.BidInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service("bidInfoServiceImpl")
public class BidInfoServiceImpl implements BidInfoService{

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Override
    public Double queryBidAllMoney() {

        redisTemplate.setKeySerializer(new StringRedisSerializer());

        BoundValueOperations<String, Object> boundValueOps = redisTemplate.boundValueOps(Constant.BID_ALL_MONEY);

        Double bidAllMoney = (Double) boundValueOps.get();
        if (bidAllMoney == null) {

            bidAllMoney= bidInfoMapper.selectBidAllMoney();

            boundValueOps.set(bidAllMoney);
            boundValueOps.expire(15, TimeUnit.SECONDS);

        }
        return bidAllMoney;
    }

    @Override
    public List<BidInfo> queryBidInfoListByLoanId(Integer id) {
        return bidInfoMapper.selectBidInfoListByLoanId(id);
    }
}
