package com.flashpoint.p2p.web;

import com.flashpoint.p2p.common.Constant;
import com.flashpoint.p2p.service.loan.LoanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private LoanInfoService loanInfoService;

    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request, Model model){

        //获取历史平均年化收益率
        Double historyAverageRate=loanInfoService.queryHistoryAverageRate();

        model.addAttribute(Constant.HISTORY_AVERAGE_RATE,historyAverageRate);

        return "test";



    }


}
