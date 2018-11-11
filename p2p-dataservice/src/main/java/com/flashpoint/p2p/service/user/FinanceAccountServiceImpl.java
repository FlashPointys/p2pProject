package com.flashpoint.p2p.service.user;

import com.flashpoint.p2p.mapper.user.FinanceAccountMapper;
import com.flashpoint.p2p.model.user.FinanceAccount;
import com.flashpoint.p2p.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinanceAccountServiceImpl implements FinanceAccountService{
    @Autowired
    private FinanceAccountMapper financeAccountMapper;
    @Override
    public FinanceAccount queryFinanceAccountByUid(User user) {

        return financeAccountMapper.selectFinaceAccountByUid(user);
    }
}
