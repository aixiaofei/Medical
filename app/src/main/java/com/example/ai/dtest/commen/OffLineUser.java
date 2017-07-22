package com.example.ai.dtest.commen;

import org.litepal.crud.DataSupport;

/**
 * Created by ai on 2017/7/11.
 */

public class OffLineUser extends DataSupport{
    private String userName;
    private String passWord;
    private String Token;
    private int isAutoLogin;
    private String lastLoginTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public int getIsAutoLogin() {
        return isAutoLogin;
    }

    public void setIsAutoLogin(int isAutoLogin) {
        this.isAutoLogin = isAutoLogin;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
