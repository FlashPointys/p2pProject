package com.flashpoint.p2p.web;

import com.alibaba.fastjson.JSONObject;
import com.flashpoint.p2p.common.Constant;
import com.flashpoint.p2p.model.loan.RechargeRecord;
import com.flashpoint.p2p.model.user.User;
import com.flashpoint.p2p.service.loan.OnlyNumberService;
import com.flashpoint.p2p.service.loan.RechargeRecordService;
import com.flashpoint.p2p.util.DateUtils;
import com.flashpoint.p2p.util.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RechargeController {
    @Autowired
    private OnlyNumberService onlyNumberService;
    @Autowired
    private RechargeRecordService rechargeRecordService;

    @RequestMapping("/loan/toAlipayRecharge")
    public String toAlipayRecharge(HttpServletRequest request, Model model,
                                   @RequestParam(value = "rechargeMoney",required = true) Double rechargeMoney){
        //获取user标识
        User sessionUser= (User) request.getSession().getAttribute(Constant.SESSION_USER);

        //生成充值唯一订单编号=时间戳+redis全局唯一数字
        String rechargeNo= DateUtils.getTimeStamp()+onlyNumberService.getOnlyNumber();

       //生成充值记录
        RechargeRecord rechargeRecord=new RechargeRecord();
        rechargeRecord.setRechargeDesc("支付宝充值");
        rechargeRecord.setRechargeMoney(rechargeMoney);
        rechargeRecord.setRechargeNo(rechargeNo);
        rechargeRecord.setRechargeStatus("0");//0为充值
        rechargeRecord.setRechargeTime(new Date());
        rechargeRecord.setUid(sessionUser.getId());

        int rechargeRecordCount= rechargeRecordService.addRechargeRecord(rechargeRecord);

        if(rechargeRecordCount>0){

            //向p2p-pay传递参数
            System.out.println("向p2p-pay传递参数");
            model.addAttribute("p2p_pay_alipay_url","http://localhost:9090/pay/api/alipay");
            model.addAttribute("out_trade_no",rechargeNo);
            model.addAttribute("rechargeMoney",rechargeMoney);
            model.addAttribute("subject","支付宝充值");
            model.addAttribute("body","支付宝充值");


        }else{
            model.addAttribute("trade_msg","充值失败!");
            return "toRechargeBack";
        }
        return "toAlipay";
    }

    @RequestMapping(value ="loan/alipayBack")
    public String alipayBack(HttpServletRequest request,Model model,
                             @RequestParam (value = "out_trade_no",required = true) String out_trade_no,
                             @RequestParam (value = "total_amount",required = true) Double total_amount,
                             @RequestParam (value = "signVerified",required = true) String signVerified){

        System.out.println("----------------------------out_trade_no"+out_trade_no+",total_amount"+total_amount+",signVerified"+signVerified);


        //判断签名是否正确
        if(StringUtils.equalsIgnoreCase(Constant.SUCCESS,signVerified)){
            //调用pay工程订单查询接口
            Map<String,Object> paramMap=new HashMap<>();
            paramMap.put("out_trade_no",out_trade_no);
           String jsonString=  HttpClientUtils.doPost("http://localhost:9090/pay/api/alipayQuery",paramMap);

            //解析json格式的字符串
            //将json格式的字符串转换为json对象
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            JSONObject tradeQueryResponse = jsonObject.getJSONObject("alipay_trade_query_response");
            //获取通信标识code
            String code = tradeQueryResponse.getString("code");
            //判断通信是否成功
            if(StringUtils.equalsIgnoreCase("10000",code)){
                //获取业务处理结果trade_status
                String tradeStatus = tradeQueryResponse.getString("trade_status");

               /* 交易状态：
                WAIT_BUYER_PAY（交易创建，等待买家付款）
                TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）
                TRADE_SUCCESS（交易支付成功）
                TRADE_FINISHED（交易结束，不可退款）*/

                if ("TRADE_CLOSED".equals(tradeStatus)) {
                    //更新充值记录的状态为2
                    RechargeRecord updateRechargeRecord = new RechargeRecord();
                    updateRechargeRecord.setRechargeNo(out_trade_no);
                    updateRechargeRecord.setRechargeStatus("2");
                    int modifyRechargeCount = rechargeRecordService.modifyRechargeRecordByRechargeNo(updateRechargeRecord);

                    if (modifyRechargeCount <= 0) {
                        model.addAttribute("trade_msg","充值失败");
                        return "toRechargeBack";
                    }
                }


                if ("TRADE_SUCCESS".equals(tradeStatus)) {
                    //从session中获取用户信息
                    User sessionUser = (User) request.getSession().getAttribute(Constant.SESSION_USER);

                    //给用户充值【1.更新帐户可用余额 2.更新充值记录的状态】(用户标识，充值金额，充值订单号)
                    paramMap.put("uid",sessionUser.getId());
                    paramMap.put("rechargeMoney",total_amount);
                    paramMap.put("rechargeNo",out_trade_no);
                    paramMap.put("rechargeStatus","1");
                    int rechargeCount = rechargeRecordService.recharge(paramMap);

                    if (rechargeCount <= 0) {
                        model.addAttribute("trade_msg","充值失败");
                        return "toRechargeBack";
                    }
                }



            } else {
                model.addAttribute("trade_msg","充值失败");
                return "toRechargeBack";
            }


        } else {
            model.addAttribute("trade_msg","充值失败");
            return "toRechargeBack";
        }





        return "redirect:/loan/myCenter";
            }

        }





