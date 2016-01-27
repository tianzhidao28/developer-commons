package cn.jpush.commons.utils;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by rocyuan on 16/1/26.
 */
public class DateUtils {


    public static Date parseDate(String dateStr, String pattern) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {

        }
        return null;
    }

    public static String formatDate(Date date, String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.format(date);
        } catch (Exception e) {

        }
        return null;
    }


    // start,end:20150123
    public static List<Long> getDaySet(String start, String end) {
        List<Long> result = new ArrayList<Long>();
        Date sDate = parseDate(start, "yyyyMMdd");
        Date eDate = parseDate(end, "yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sDate);
        while (eDate.after(calendar.getTime())) {
            result.add(Long.valueOf(formatDate(calendar.getTime(), "yyyyMMdd")));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        result.add(Long.valueOf(formatDate(eDate, "yyyyMMdd")));
        return result;
    }

    // start,end:20150123
    public static List<Long> getMonSet(String start, String end) {
        Date sDate = parseDate(start.substring(0, 6), "yyyyMM");
        Date eDate = parseDate(end.substring(0, 6), "yyyyMM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sDate);
        List<Long> result = new ArrayList<Long>();
        while (eDate.after(calendar.getTime())) {
            result.add(Long.valueOf(formatDate(calendar.getTime(), "yyyyMM")));
            calendar.add(Calendar.MONTH, 1);
        }
        result.add(Long.valueOf(formatDate(eDate, "yyyyMM")));
        return result;
    }

    // start,end:20150123
    public static List<Long> getHourSet(String start, String end) {
        Date sDate = parseDate(start, "yyyyMMdd");
        Date eDate = parseDate(end, "yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sDate);
        String[] hour = { "01", "02", "03", "04", "05", "06", "07", "08", "09",
                "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
                "20", "21", "22", "23", "24" };
        List<Long> result = new ArrayList<Long>();
        while (eDate.after(calendar.getTime())) {
            String day = formatDate(calendar.getTime(), "yyyyMMdd");
            for (String str : hour) {
                result.add(Long.valueOf(day + str));
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return result;
    }

    public static void main(String args[]) {
        List<Long> l = getDaySet("20150701", "20150704");
        System.out.println(l);
    }
}
