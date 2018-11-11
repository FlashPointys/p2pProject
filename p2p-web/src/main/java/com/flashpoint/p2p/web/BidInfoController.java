package com.flashpoint.p2p.web;

import com.flashpoint.p2p.common.Constant;
import com.flashpoint.p2p.model.loan.BidInfo;
import com.flashpoint.p2p.model.user.User;
import com.flashpoint.p2p.model.vo.PaginationVO;
import com.flashpoint.p2p.model.vo.ResultObject;
import com.flashpoint.p2p.service.loan.BidInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class BidInfoController {

    @Autowired
    private BidInfoService bidInfoService;

    @RequestMapping("loan/myInvest")
    public String myInvest(HttpServletRequest request, Model model,
                           @RequestParam(value = "currentPage",required = false) Integer currentPage){


        if(currentPage==null){
            currentPage=1;
        }
        //通过session获取user
        User sessionUser = (User) request.getSession().getAttribute(Constant.SESSION_USER);

        Map<String,Object> paramMap=new HashMap<>();
        Integer pageSize=10;
        paramMap.put("currentPage",(currentPage-1)*pageSize);
        paramMap.put("pageSize",pageSize);
        paramMap.put("uid",sessionUser.getId());
         PaginationVO<BidInfo> paginationVO = bidInfoService.queryBidinfoListByPage(paramMap);

         int totalPage=paginationVO.getTotal().intValue()/pageSize;
         int mod=paginationVO.getTotal().intValue()%pageSize;
         if(mod!=0){
             totalPage=totalPage+1;
         }


        model.addAttribute("currentPage",currentPage);
         model.addAttribute("totalRows",paginationVO.getTotal());
         model.addAttribute("bidInfoList",paginationVO.getDataList());
         model.addAttribute("totalPage",totalPage);
        return "myInvest";
    }

    @RequestMapping("loan/invest")
    @ResponseBody
    public Object invest(HttpServletRequest request,
                         @RequestParam(value = "bidMoney",required = true) Double bidMoney,
                         @RequestParam(value = "loanId",required = true) Integer loanId){

        Map<String,Object> retMap=new HashMap<>();

        User sessionUser= (User) request.getSession().getAttribute(Constant.SESSION_USER);

        retMap.put("bidMoney",bidMoney);
        retMap.put("loanId",loanId);
        retMap.put("uid",sessionUser.getId());
        retMap.put("phone",sessionUser.getPhone());


        ResultObject resultObject= bidInfoService.invest(retMap);


        if(StringUtils.equalsIgnoreCase(Constant.SUCCESS,resultObject.getErrorCode())){
            retMap.put(Constant.ERROR_MESSAGE,Constant.OK);
        }else{
            retMap.put(Constant.ERROR_MESSAGE,"投资人数过多,请稍后再试...");
            return retMap;
        }
        return retMap;
    }

}
