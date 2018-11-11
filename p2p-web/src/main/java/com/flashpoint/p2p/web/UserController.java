package com.flashpoint.p2p.web;

import com.alibaba.fastjson.JSONObject;
import com.flashpoint.p2p.common.Constant;
import com.flashpoint.p2p.model.loan.BidInfo;
import com.flashpoint.p2p.model.loan.IncomeRecord;
import com.flashpoint.p2p.model.loan.RechargeRecord;
import com.flashpoint.p2p.model.user.FinanceAccount;
import com.flashpoint.p2p.model.user.User;
import com.flashpoint.p2p.model.vo.ResultObject;
import com.flashpoint.p2p.service.loan.BidInfoService;
import com.flashpoint.p2p.service.loan.IncomeRecordService;
import com.flashpoint.p2p.service.loan.LoanInfoService;
import com.flashpoint.p2p.service.loan.RechargeRecordService;
import com.flashpoint.p2p.service.user.FinanceAccountService;
import com.flashpoint.p2p.service.user.UserService;
import com.flashpoint.p2p.util.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private LoanInfoService loanInfoService;
    @Autowired
    private BidInfoService bidInfoService;
    @Autowired
    private FinanceAccountService financeAccountService;

    @Autowired
    private RechargeRecordService rechargeRecordService;

    @Autowired
    private IncomeRecordService incomeRecordService;

    /**
     * 验证手机号码是否存在
     *
     * @param request
     * @param phone
     * @return
     */
    @RequestMapping("loan/checkPhone")
    @ResponseBody
    public Object checkPhone(HttpServletRequest request,
                             @RequestParam(value = "phone", required = true) String phone) {

        Map<String, Object> paramMap = new HashMap<>();
        User user = null;
        user = userService.queryUserByPhone(phone);

        if (user != null) {
            //存在的用户
            paramMap.put(Constant.ERROR_MESSAGE, "该手机号码已存在，请更换手机号码");
            return paramMap;
        }
        paramMap.put(Constant.ERROR_MESSAGE, Constant.OK);

        return paramMap;
    }

    @RequestMapping("loan/checkCaptcha")
    @ResponseBody
    public Object checkCaptcha(HttpServletRequest request,
                               @RequestParam(value = "captcha", required = true) String captcha) {
        Map<String, Object> paramMap = new HashMap<>();
        //从session域中获取验证码
        String sessionCaptcha = (String) request.getSession().getAttribute(Constant.CAPTCHA);

        //进行比对
        if (!StringUtils.equalsIgnoreCase(captcha, sessionCaptcha)) {
            paramMap.put(Constant.ERROR_MESSAGE, "验证码不正确");
            return paramMap;
        }
        paramMap.put(Constant.ERROR_MESSAGE, Constant.OK);
        return paramMap;
    }

    @RequestMapping("loan/register")
    @ResponseBody
    public Object register(HttpServletRequest request,
                           @RequestParam(value = "phone", required = true) String phone,
                           @RequestParam(value = "loginPassword", required = true) String loginPassword,
                           @RequestParam(value = "replayLoginPassword", required = true) String replayLoginPassword) {

        Map<String, Object> paramMap = new HashMap<>();
        if (!Pattern.matches("^1[1-9]\\d{9}$", phone)) {
            paramMap.put(Constant.ERROR_MESSAGE, "手机号码格式不正确");
            return paramMap;
        }
        if (!StringUtils.equalsIgnoreCase(loginPassword, replayLoginPassword)) {
            paramMap.put(Constant.ERROR_MESSAGE, "两次密码不一样");
            return paramMap;
        }

        ResultObject resultObject = userService.register(phone, loginPassword);

        if (!StringUtils.equalsIgnoreCase(Constant.OK, resultObject.getErrorCode())) {
            paramMap.put(Constant.ERROR_MESSAGE, "用户注册失败,请重试");
            return paramMap;
        }

        //将用户放进session
        request.getSession().setAttribute(Constant.SESSION_USER, userService.queryUserByPhone(phone));

        paramMap.put(Constant.ERROR_MESSAGE, Constant.OK);

        return paramMap;
    }

    @RequestMapping("loan/verifyRealName")
    @ResponseBody
    public Object verifyRealName(HttpServletRequest request,
                                 @RequestParam(value = "realName", required = true) String realName,
                                 @RequestParam(value = "idCard", required = true) String idCard,
                                 @RequestParam(value = "replayIdCard", required = true) String replayIdCard) {

        Map<String, Object> paramMap = new HashMap();

        if(!Pattern.matches("^[\\u4e00-\\u9fa5]{0,}$",realName)){
            paramMap.put(Constant.ERROR_MESSAGE,"姓名只支持中文");
            return paramMap;

        }
        if(Pattern.matches("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)",idCard)){
            paramMap.put(Constant.ERROR_MESSAGE,"身份证格式不正确");
            return paramMap;
        }

        if(!StringUtils.equalsIgnoreCase(idCard,replayIdCard)){
           paramMap.put(Constant.ERROR_MESSAGE,"输入身份证号码不一致");
           return paramMap;
       }

       Map<String,Object> map=new HashMap<>();
        map.put("appkey","48af9aad0820cf50e96854d5ef4e20d1");
        map.put("cardNo",idCard);
        map.put("realName",realName);
        String jsonStr = HttpClientUtils.doPost("https://way.jd.com/youhuoBeijing/test", map);


        JSONObject jsonObject = JSONObject.parseObject(jsonStr);

        String code= (String) jsonObject.get("code");

        //判断通信是否成功
        if(StringUtils.equalsIgnoreCase("10000",code)){

            //判断业务处理结果
            Boolean isok= jsonObject.getJSONObject("result").getJSONObject("result").getBoolean("isok");

            if(isok){
                //业务处理
               User sessionUser= (User) request.getSession().getAttribute(Constant.SESSION_USER);
               User updateUser=new User();
                updateUser.setId(sessionUser.getId());
                updateUser.setIdCard(idCard);
                updateUser.setName(realName);

              int modifyUserCount=  userService.modifyUserByUid(updateUser);

              if(modifyUserCount>0){
                  //存放到session中
                  request.getSession().setAttribute(Constant.SESSION_USER,userService.queryUserByPhone(sessionUser.getPhone()));

                  paramMap.put(Constant.ERROR_MESSAGE,Constant.OK);
                  return paramMap;

              }else{
                  paramMap.put(Constant.ERROR_MESSAGE,"实名认证人数过多，请稍后重试...");
                  return paramMap;
              }



            }
            else{
                paramMap.put(Constant.ERROR_MESSAGE,"实名认证人数过多，请稍后重试...");
                return paramMap;
            }
        }else{
            paramMap.put(Constant.ERROR_MESSAGE,"通信异常,请稍后再试...");
            return paramMap;
        }


    }

    @RequestMapping("loan/loadStat")
    @ResponseBody
    public Object loadStat(HttpServletRequest request){

        Map<String,Object> paramMap=new HashMap<>();

        Long allUserCont= userService.queryAllUseCount();
        Double historyAverageRate= loanInfoService.queryHistoryAverageRate();

        Double bidAllMoney= bidInfoService.queryBidAllMoney();
        paramMap.put(Constant.ALL_USER_COUNT,allUserCont);
        paramMap.put(Constant.HISTORY_AVERAGE_RATE,historyAverageRate);
        paramMap.put(Constant.BID_ALL_MONEY,bidAllMoney);
        return paramMap;

    }

    @RequestMapping("loan/login")
    @ResponseBody
    public Object login(HttpServletRequest request,
                        @RequestParam(value = "phone",required = true) String phone,
                        @RequestParam(value = "loginPassword",required = true )String loginPassword){

        Map<String,Object> paramMap=new HashMap<>();

        if(!Pattern.matches("^1[1-9]\\d{9}$",phone)){
            paramMap.put(Constant.ERROR_MESSAGE,"请输入正确的手机号码");
            return paramMap;
        }
        System.out.println("====================="+loginPassword);
        User user= userService.login(phone,loginPassword);

        if(user==null){
            paramMap.put(Constant.ERROR_MESSAGE,"用户名,密码不正确");
            return paramMap;
        }


        request.getSession().setAttribute(Constant.SESSION_USER,user);
        paramMap.put(Constant.ERROR_MESSAGE,Constant.OK);
        return paramMap;
    }

    @RequestMapping("loan/logout")
    public String logout(HttpServletRequest request){
        request.getSession().removeAttribute(Constant.SESSION_USER);
        return "redirect:/index";
    }

    @RequestMapping("loan/myCenter")
    public String myCenter(HttpServletRequest request, Model model){
        User sessionUser= (User) request.getSession().getAttribute(Constant.SESSION_USER);

       //获取用户账户的资金

        FinanceAccount financeAccount= financeAccountService.queryFinanceAccountByUid(sessionUser);

        model.addAttribute("financeAccount",financeAccount);

        //准备分页数据
        Map<String,Object> map=new HashMap<>();
        int pageSize=5;
        map.put("currentPage",0);
        map.put("pageSize",pageSize);
        map.put("uid",sessionUser.getId());

         List<BidInfo> bidInfoList= bidInfoService.queryBidinfoListByUid(map);

         model.addAttribute("bidInfoList",bidInfoList);

        List<RechargeRecord> rechargeRecordList= rechargeRecordService.queryRechargeRecourdListByUid(map);

        model.addAttribute("rechargeRecordList",rechargeRecordList);

        List<IncomeRecord> incomeRecordList= incomeRecordService.queryIncomeRecordServiceByUid(map);

        model.addAttribute("incomeRecordList",incomeRecordList);

        return "myCenter";
    }

}
