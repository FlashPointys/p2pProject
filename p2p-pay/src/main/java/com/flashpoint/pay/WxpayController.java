package com.flashpoint.pay;

import com.flashpoint.p2p.util.HttpClientUtils;
import com.github.wxpay.sdk.WXPayUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class WxpayController {

    private Logger logger=Logger.getLogger(WxpayController.class);

    @RequestMapping(value = "/api/wxpay")
    @ResponseBody
    public Object wxpay(HttpServletRequest request,
                        @RequestParam(value = "body",required = true)String body,
                        @RequestParam(value = "out_trade_no",required = true)String out_trade_no,
                        @RequestParam(value = "total_fee",required = true) Double total_fee) throws Exception {

        Map<String,String> requestDataMap = new HashMap<String,String>();
        requestDataMap.put("appid","wx8a3fcf509313fd74");
        requestDataMap.put("mch_id","1361137902");
        requestDataMap.put("nonce_str", WXPayUtil.generateNonceStr());
        requestDataMap.put("body",body);
        requestDataMap.put("out_trade_no",out_trade_no);
        BigDecimal bigDecimal = new BigDecimal(total_fee);
        BigDecimal multiply = bigDecimal.multiply(new BigDecimal(100));
        requestDataMap.put("total_fee",multiply.toString());
        requestDataMap.put("spbill_create_ip","127.0.0.1");
        requestDataMap.put("notify_url","http://localhost:9090/pay/api/wxpayNotify");
        requestDataMap.put("trade_type","NATIVE");
        requestDataMap.put("product_id",out_trade_no);
        String sign = WXPayUtil.generateSignature(requestDataMap,"367151c5fd0d50f1e34a68a802d6bbca");
        requestDataMap.put("sign",sign);

        //将map集合的请求参数转换为xml
        String requestDataXml = WXPayUtil.mapToXml(requestDataMap);

        //调用统一下单API接口,响应的是xml格式的字符串
        String responseDataXml = HttpClientUtils.doPostByXml("https://api.mch.weixin.qq.com/pay/unifiedorder", requestDataXml);

        //将xml格式的字符串转换为map集合
        Map<String, String> responseDataMap = WXPayUtil.xmlToMap(responseDataXml);

        return responseDataMap;

    }
    @RequestMapping(value = "/api/wxpayNotify")
    public String wxpayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {

        InputStream inputStream =  request.getInputStream();
        //BufferedReader是包装设计模式，性能更搞
        BufferedReader in =  new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
        StringBuffer sb = new StringBuffer();

        //1、将微信回调信息转为字符串
        String line ;
        while ((line = in.readLine()) != null){
            sb.append(line);
        }
        in.close();
        inputStream.close();

        //2、将xml格式字符串格式转为map集合
        Map<String,String> callbackMap = WXPayUtil.xmlToMap(sb.toString());

        //3、转为有序的map
//        SortedMap<String,String> sortedMap = WXPayUtil.getSortedMap(callbackMap);

        //4、判断签名是否正确
        /*if(WXPayUtil.isCorrectSign(sortedMap,weChatConfig.getKey())){
            //5、判断回调信息是否成功
            if("SUCCESS".equals(sortedMap.get("result_code"))){
                //获取商户订单号
                //商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一
                String outTradeNo = sortedMap.get("out_trade_no");
                System.out.println(outTradeNo);
                //6、数据库查找订单,如果存在则根据订单号更新该订单
                VideoOrder dbVideoOrder = videoOrderService.findByOutTradeNo(outTradeNo);
                System.out.println(dbVideoOrder);
                if(dbVideoOrder != null && dbVideoOrder.getState()==0){  //判断逻辑看业务场景
                    VideoOrder videoOrder = new VideoOrder();
                    videoOrder.setOpenid(sortedMap.get("openid"));
                    videoOrder.setOutTradeNo(outTradeNo);
                    videoOrder.setNotifyTime(new Date());
                    //修改支付状态，之前生成的订单支付状态是未支付，这里表面已经支付成功的订单
                    videoOrder.setState(1);
                    //根据商户订单号更新订单
                    int rows = videoOrderService.updateVideoOderByOutTradeNo(videoOrder);
                    System.out.println(rows);
                    //7、通知微信订单处理成功
                    if(rows == 0){
                        response.setContentType("text/xml");
                        response.getWriter().println("success");
                        return;
                    }
                }}
//        }
        //7、通知微信订单处理失败
        response.setContentType("text/xml");
        response.getWriter().println("fail");*/

        String resXml = "";
        if("SUCCESS".equals((String)callbackMap.get("result_code"))){
            // 这里是支付成功
            //////////执行自己的业务逻辑////////////////
            /*String mch_id = (String)callbackMap.get("mch_id");
            String openid = (String)callbackMap.get("openid");
            String is_subscribe = (String)callbackMap.get("is_subscribe");
            String out_trade_no = (String)callbackMap.get("out_trade_no");

            String total_fee = (String)callbackMap.get("total_fee");

            logger.info("mch_id:"+mch_id);
            logger.info("openid:"+openid);
            logger.info("is_subscribe:"+is_subscribe);
            logger.info("out_trade_no:"+out_trade_no);
            logger.info("total_fee:"+total_fee);*/

            //////////执行自己的业务逻辑////////////////

            logger.info("支付成功");
            //通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
            resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                    + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

        } else {
            logger.info("支付失败,错误信息：" + callbackMap.get("err_code"));
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        //------------------------------
        //处理业务完毕
        //------------------------------
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();


        return "toWxpayP2P";
    }
}
