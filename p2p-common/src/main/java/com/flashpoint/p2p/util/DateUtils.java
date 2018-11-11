package com.flashpoint.p2p.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date getDateByAddDays(Date date, Integer days) {

        //创建日期处理对象
        Calendar calendar=Calendar.getInstance();
        //设置日期对象的值
        calendar.setTime(date);
        //添加的天数
         calendar.add(Calendar.DAY_OF_MONTH,days);
         return calendar.getTime();
    }

    public static Date getDateByAddMontys(Date date, Integer month) {
        Calendar calendar=Calendar.getInstance();

        calendar.setTime(date);

        calendar.add(Calendar.MONTH,month);

        return  calendar.getTime();
    }
}
