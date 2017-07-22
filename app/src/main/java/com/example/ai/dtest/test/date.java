package com.example.ai.dtest.test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ai on 2017/7/15.
 */

public class date {
    public static void main(String[] args) throws ParseException {
        Date buf_date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        ParsePosition position= new ParsePosition(8);
        String date_1 = dateFormat.format(buf_date);
        Date res= dateFormat.parse(date_1);
        System.out.println(res);
    }
}
