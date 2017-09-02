package com.xing.middleware.framework.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jecceca on 2017/8/28.
 */
public class Utils {
    private static DateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");

    public static String formatDate(Date date) {
        synchronized (simpleDateFormat) {
            return simpleDateFormat.format(date);
        }
    }



    public static String getThrowableDetail(Throwable e) {
        StringWriter writer = new StringWriter(2048);
        e.printStackTrace(new PrintWriter(writer));
        String detailMessage = writer.toString();
        return detailMessage;
    }

}
