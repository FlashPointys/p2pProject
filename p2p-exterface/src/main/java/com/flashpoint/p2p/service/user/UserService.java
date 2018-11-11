package com.flashpoint.p2p.service.user;

import com.flashpoint.p2p.model.user.User;
import com.flashpoint.p2p.model.vo.ResultObject;

public interface UserService {
    /**
     * 查询用户数量
     * @return
     */
    Long queryAllUseCount();

    /**
     * 通过手机号码返回用户
     * @param phone
     * @return
     */
    User queryUserByPhone(String phone);

    /**
     * 进行用户注册
     * @param phone
     * @param loginPassword
     * @return
     */
    ResultObject register(String phone, String loginPassword);

    /**
     * 通过id更改用户信息
     * @param updateUser
     * @return
     */
    int modifyUserByUid(User updateUser);

    /**
     * 登录
     * @param phone
     * @param loginPassword
     * @return
     */
    User login(String phone, String loginPassword);
}
