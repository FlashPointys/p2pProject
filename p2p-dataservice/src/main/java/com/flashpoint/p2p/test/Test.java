package com.flashpoint.p2p.test;

import com.flashpoint.p2p.service.loan.BidInfoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
    public static void main(String[] args) {

        ClassPathXmlApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        BidInfoService bidInfoService= (BidInfoService) applicationContext.getBean("bidInfoServiceImpl");

        Map<String,Object> paramMap=new HashMap<>();
        paramMap.put("uid",6);
        paramMap.put("loanId",3);
        paramMap.put("bidMoney",1.0);

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        for(int i=0;i<=1000;i++) {
            executorService.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return bidInfoService.invest(paramMap);
                }
            });
        }
        executorService.shutdown();
    }
}

