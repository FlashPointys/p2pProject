package com.flashpoint.p2p.service.user;

import com.flashpoint.p2p.common.Constant;
import com.flashpoint.p2p.mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service("userServiceImpl")
public class UserServiceImpl implements  UserService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Override
    public Long queryAllUseCount() {

         Long allUserCount= (Long) redisTemplate.opsForValue().get(Constant.ALL_USER_COUNT);

        if (allUserCount == null) {

            allUserCount= userMapper.selectAllUserCount();

            redisTemplate.opsForValue().set(Constant.ALL_USER_COUNT,allUserCount,15, TimeUnit.SECONDS);




        }

         return allUserCount;
    }
}
