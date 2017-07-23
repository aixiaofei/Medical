package com.example.ai.dtest.commen;

import org.litepal.crud.DataSupport;

/**
 * Created by ai on 2017/7/11.
 */

public class OffLineUser extends DataSupport{

    private String userPhone;
    private String password;
    private String Token;
    private int isAutoLogin;
    private String lastLoginTime;

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
