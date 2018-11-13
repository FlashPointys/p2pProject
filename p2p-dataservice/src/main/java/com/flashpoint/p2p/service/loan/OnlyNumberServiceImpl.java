package com.flashpoint.p2p.service.loan;

import com.flashpoint.p2p.common.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class OnlyNumberServiceImpl implements OnlyNumberService{
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Override
    public Long getOnlyNumber() {
        return redisTemplate.opsForValue().increment(Constant.ONLY_NUMBER,1);
    }
}
