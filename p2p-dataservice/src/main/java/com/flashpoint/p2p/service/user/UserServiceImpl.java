package com.flashpoint.p2p.service.user;

import com.flashpoint.p2p.common.Constant;
import com.flashpoint.p2p.mapper.user.FinanceAccountMapper;
import com.flashpoint.p2p.mapper.user.UserMapper;
import com.flashpoint.p2p.model.user.FinanceAccount;
import com.flashpoint.p2p.model.user.User;
import com.flashpoint.p2p.model.vo.ResultObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service("userServiceImpl")
public class UserServiceImpl implements  UserService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;
    @Override
    public Long queryAllUseCount() {

         Long allUserCount= (Long) redisTemplate.opsForValue().get(Constant.ALL_USER_COUNT);

        if (allUserCount == null) {

            allUserCount= userMapper.selectAllUserCount();

            redisTemplate.opsForValue().set(Constant.ALL_USER_COUNT,allUserCount,15, TimeUnit.SECONDS);




        }

         return allUserCount;
    }

    @Override
    public User queryUserByPhone(String phone) {
        return userMapper.selectUserByPhone(phone);
    }

    @Override
    public ResultObject register(String phone, String loginPassword) {
        ResultObject resultObject=new ResultObject();
        resultObject.setErrorCode(Constant.SUCCESS);
        User user=new User();
        user.setPhone(phone);
        user.setLoginPassword(loginPassword);
        user.setAddTime(new Date());
        user.setLastLoginTime(new Date());

        //新增用户
        int insertUserCount= userMapper.insertSelective(user);

        if(insertUserCount>0){

            user= userMapper.selectUserByPhone(phone);
            //开账户
            FinanceAccount financeAccount=new FinanceAccount();
            financeAccount.setAvailableMoney(888.0);
            financeAccount.setUid(user.getId());

           int insertFinanceAccountCount= financeAccountMapper.insertSelective(financeAccount);
            if(insertFinanceAccountCount<=0){
                resultObject.setErrorCode(Constant.FAIL);
            }

        }else{
            resultObject.setErrorCode(Constant.FAIL);
        }

        return resultObject;
    }

    @Override
    public int modifyUserByUid(User updateUser) {

        return userMapper.updateByPrimaryKeySelective(updateUser);
    }

    @Override
    public User login(String phone, String loginPassword) {

        User user= userMapper.selectUserByPhoneAndLoginPassword(phone,loginPassword);

        if(user!=null){
            User updateUser=new User();
            updateUser.setId(user.getId());
            updateUser.setLastLoginTime(new Date());
            userMapper.updateByPrimaryKeySelective(updateUser);
        }
        return user;

    }


}
