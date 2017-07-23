package com.example.ai.dtest.yanzhengma;

/**
 * Created by ai on 2017/7/10.
 */
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class FormatCheckUtils {
    /**
     * 大陆号码或香港号码均可
     */
    public static boolean isPhoneLegal(String str)throws PatternSyntaxException {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
    }

    public static boolean isNumber(String str) throws PatternSyntaxException{
        String regExp= "^[0-9]+$";
        Pattern p= Pattern.compile(regExp);
        Matcher m= p.matcher(str);
        return m.matches();
    }

    public static boolean isPassword(String str) throws  PatternSyntaxException{
        boolean isLength= false;
        String regExp = "^[A-Za-z0-9]+$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        boolean a= m.matches();
        if(str.length()<=15 && str.length()>=6){
            isLength=true;
        }
        return a&&isLength;
    }
    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+除1和4的任意数
     * 17+除9的任意数
     * 147
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str)throws PatternSyntaxException {
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
