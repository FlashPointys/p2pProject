package com.flashpoint.p2p.web;

import com.flashpoint.p2p.common.Constant;
import com.flashpoint.p2p.model.loan.LoanInfo;
import com.flashpoint.p2p.service.loan.BidInfoService;
import com.flashpoint.p2p.service.user.UserService;
import com.flashpoint.p2p.service.loan.LoanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    private LoanInfoService loanInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private BidInfoService bidInfoService;

    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request, Model model){

        //获取历史平均年化收益率
        Double historyAverageRate=loanInfoService.queryHistoryAverageRate();

        model.addAttribute(Constant.HISTORY_AVERAGE_RATE,historyAverageRate);

        //获取用户数量

        Long allUserCount= userService.queryAllUseCount();
        model.addAttribute(Constant.ALL_USER_COUNT,allUserCount);

        //累计成交金额

        Double bidAllMoney= bidInfoService.queryBidAllMoney();

        model.addAttribute(Constant.BID_ALL_MONEY,bidAllMoney);

       //queryLoanInfoListByProductType  selectLoanInfoByPage

        //将下列查询看成分页查询
        //准备查询数据
        Map<String,Object> paraMap=new HashMap<>();

        paraMap.put("currentPage",0);



        //新手宝的查询 每页显示一个 类型0
        paraMap.put("pageSize",1);
        paraMap.put("productType",Constant.PRODUCT_TYPE_X);
        List<LoanInfo> xLoanInfoList= loanInfoService.queryLoanInfoListByProductType(paraMap);
        model.addAttribute("xLoanInfoList",xLoanInfoList);
        //优选的查询 每页显示八个 类型 1
        paraMap.put("pageSize",4);
        paraMap.put("productType",Constant.PRODUCT_TYPE_U);
        List<LoanInfo> uLoanInfoList= loanInfoService.queryLoanInfoListByProductType(paraMap);

        model.addAttribute("uLoanInfoList",uLoanInfoList);
        //散标的查询 显示一页 每页显示8 条 类型 2
        paraMap.put("pageSize",8);
        paraMap.put("productType",Constant.PRODUCT_TYPE_S);
        List<LoanInfo> sLoanInfoList= loanInfoService.queryLoanInfoListByProductType(paraMap);

        model.addAttribute("sLoanInfoList",sLoanInfoList);

        return "index";



    }


}
