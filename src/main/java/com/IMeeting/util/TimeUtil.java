package com.IMeeting.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gjw on 2019/1/18.
 */
public class TimeUtil {
    /**
     * 根据时间类型比较时间大小
     *
     * @param source
     * @param traget
     * @param type      "YYYY-MM-DD" "yyyyMMdd HH:mm:ss"  类型可自定义
     * @param 传递时间的对比格式
     * @return 0 ：source和traget时间相同
     * 1 ：source比traget时间大
     * -1：source比traget时间小
     * @throws Exception
     */
    public static int DateCompare(String source, String traget, String type) throws Exception {
        int ret = 2;
        SimpleDateFormat format = new SimpleDateFormat(type);
        Date sourcedate = format.parse(source);
        Date tragetdate = format.parse(traget);
        ret = sourcedate.compareTo(tragetdate);
        return ret;
    }

    public static String addMinute(String beforeTime, int minute) {
        Calendar cal = Calendar.getInstance();
        int year = Integer.parseInt(beforeTime.substring(0, 4));
        int month = Integer.parseInt(beforeTime.substring(5, 7));
        int day = Integer.parseInt(beforeTime.substring(8, 10));
        int h = Integer.parseInt(beforeTime.substring(11, 13));
        int m = Integer.parseInt(beforeTime.substring(14, 16));
        cal.set(year, month, day, h, m, 0);
        cal.add(Calendar.MONTH, -1);
        cal.add(Calendar.MINUTE, minute);
        Date afterTime = (Date) cal.getTime();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String result = sf.format(afterTime);
        return result;
    }
}
