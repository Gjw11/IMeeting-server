package com.IMeeting.util;

import java.text.SimpleDateFormat;
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
     * @param type "YYYY-MM-DD" "yyyyMMdd HH:mm:ss"  类型可自定义
     * @param 传递时间的对比格式
     * @return
     *  0 ：source和traget时间相同
     *  1 ：source比traget时间大
     *  -1：source比traget时间小
     * @throws Exception
     */
    public int DateCompare(String source, String traget, String type) throws Exception {
        int ret = 2;
        SimpleDateFormat format = new SimpleDateFormat(type);
        Date sourcedate = format.parse(source);
        Date tragetdate = format.parse(traget);
        ret = sourcedate.compareTo(tragetdate);
        return ret;
    }
}
