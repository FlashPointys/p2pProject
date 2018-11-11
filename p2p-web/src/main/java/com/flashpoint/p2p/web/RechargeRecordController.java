package com.flashpoint.p2p.web;

import com.flashpoint.p2p.common.Constant;
import com.flashpoint.p2p.model.loan.RechargeRecord;
import com.flashpoint.p2p.model.user.User;
import com.flashpoint.p2p.model.vo.PaginationVO;
import com.flashpoint.p2p.service.loan.RechargeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RechargeRecordController {
    @Autowired
    private RechargeRecordService rechargeRecordService;

    @RequestMapping("loan/myRecharge")
    public String myRecharge(HttpServletRequest request, Model model,
                             @RequestParam(value = "currentPage",required = false) Integer currentPage ){

        User sessionUser= (User) request.getSession().getAttribute(Constant.SESSION_USER);

        if(currentPage==null){
            currentPage=1;
        }
        int pageSize=10;
        //准备分页数据
        Map<String,Object> paramMap=new HashMap<>();
        paramMap.put("currentPage",(currentPage-1)*pageSize);
        paramMap.put("pageSize",pageSize);
        paramMap.put("uid",sessionUser.getId());

        PaginationVO<RechargeRecord> paginationVO= rechargeRecordService.queryRechargeRecourdListByPage(paramMap);

        int totalPage=paginationVO.getTotal().intValue()/pageSize;
        int mod=paginationVO.getTotal().intValue()%pageSize;
        if(mod!=0){
            totalPage=totalPage+1;
        }
        model.addAttribute("currentPage",currentPage);
        model.addAttribute("rechargeRecordList",paginationVO.getDataList());
        model.addAttribute("totalRows",paginationVO.getTotal());
        model.addAttribute("totalPage",totalPage);
        return "myRecharge";
    }
}
