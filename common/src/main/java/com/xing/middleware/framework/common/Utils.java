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
    private static DateFormat simpleDateFormat;

    static {
        simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
//        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static boolean isNullOrEmpty(String str) {
        if (str != null && !"".equals(str.trim())) {
            return false;
        }
        return true;
    }

    public static String formatUtcDate(Date date) {
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

    public static void closeQuietly(AutoCloseable autoCloseable) {
        try {
            if (autoCloseable != null)
                autoCloseable.close();
        } catch (Throwable e) {
        }
    }

}
