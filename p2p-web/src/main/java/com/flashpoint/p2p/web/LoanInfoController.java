package com.flashpoint.p2p.web;

import com.flashpoint.p2p.model.loan.BidInfo;
import com.flashpoint.p2p.model.loan.LoanInfo;
import com.flashpoint.p2p.model.vo.PaginationVO;
import com.flashpoint.p2p.service.loan.BidInfoService;
import com.flashpoint.p2p.service.loan.LoanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoanInfoController {

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private BidInfoService bidInfoService;

    @RequestMapping("loan/loan")
    public String loan(HttpServletRequest request, Model model,
                       @RequestParam(value = "ptype",required = false) Integer ptype,
                       @RequestParam(value = "currentPage" ,required = false) Integer currentPage){


        if (currentPage == null) {
            currentPage=1;
        }
        //准备分页数据
        Map<String,Object> map=new HashMap<>();
        Integer pageSize=9;
        Integer startIndex=(currentPage-1)*pageSize;
        map.put("pageSize",pageSize);
        map.put("startIndex",startIndex);
        if(ptype!=null){
            map.put("ptype",ptype);
        }
        PaginationVO<LoanInfo> paginationVO = loanInfoService.queryLoanInfoByPage(map);


        //计算总页数
        Integer totalPage=paginationVO.getTotal().intValue()/pageSize;

        Integer mod=paginationVO.getTotal().intValue()%pageSize;
        if(mod>0){
            totalPage=totalPage+1;
        }

        model.addAttribute("totalPage",totalPage);
        model.addAttribute("totalRows",paginationVO.getTotal());
        model.addAttribute("currentPage",currentPage);
        model.addAttribute("loanInfoList",paginationVO.getDataList());
        if(ptype!=null){
            model.addAttribute("ptype",ptype);
        }



        //TODO

        return "loan";
    }

    @RequestMapping("loan/loanInfo")
    public String loanInfo(HttpServletRequest request,Model model,
                       @RequestParam(value = "id",required = true) Integer id ){

        LoanInfo loanInfo= loanInfoService.queryLoaninfoByLoanId(id);

        model.addAttribute("loanInfo",loanInfo);


        List<BidInfo> bidInfoList= bidInfoService.queryBidInfoListByLoanId(id);

        model.addAttribute("bidInfoList",bidInfoList);

        return "loanInfo";
    }
}
