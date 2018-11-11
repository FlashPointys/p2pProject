package com.flashpoint.p2p.service.user;


import com.flashpoint.p2p.model.user.FinanceAccount;
import com.flashpoint.p2p.model.user.User;

import java.util.Map;

public interface FinanceAccountService {
    /**
     * 根据用户ID查询账户
     * @param user
     * @return
     */
    public FinanceAccount queryFinanceAccountByUid(User user) ;


}
