package commen;

import java.util.Date;

public class TUserlogininfo {
    private Integer userloginid;

    private String username;

    private String userpassword;

    private String usertoken;

    private Date userlastlogintime;

    private String userlastloginlocation;

    private String userlogindevice;

    private String userbrowserversion;

    private String userphonemodel;

    private String userphoneversion;

    private String usermac;

    private String userip;

    private Integer usertype;

    public Integer getUserloginid() {
        return userloginid;
    }

    public void setUserloginid(Integer userloginid) {
        this.userloginid = userloginid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword == null ? null : userpassword.trim();
    }

    public String getUsertoken() {
        return usertoken;
    }

    public void setUsertoken(String usertoken) {
        this.usertoken = usertoken == null ? null : usertoken.trim();
    }

    public Date getUserlastlogintime() {
        return userlastlogintime;
    }

    public void setUserlastlogintime(Date userlastlogintime) {
        this.userlastlogintime = userlastlogintime;
    }

    public String getUserlastloginlocation() {
        return userlastloginlocation;
    }

    public void setUserlastloginlocation(String userlastloginlocation) {
        this.userlastloginlocation = userlastloginlocation == null ? null : userlastloginlocation.trim();
    }

    public String getUserlogindevice() {
        return userlogindevice;
    }

    public void setUserlogindevice(String userlogindevice) {
        this.userlogindevice = userlogindevice == null ? null : userlogindevice.trim();
    }

    public String getUserbrowserversion() {
        return userbrowserversion;
    }

    public void setUserbrowserversion(String userbrowserversion) {
        this.userbrowserversion = userbrowserversion == null ? null : userbrowserversion.trim();
    }

    public String getUserphonemodel() {
        return userphonemodel;
    }

    public void setUserphonemodel(String userphonemodel) {
        this.userphonemodel = userphonemodel == null ? null : userphonemodel.trim();
    }

    public String getUserphoneversion() {
        return userphoneversion;
    }

    public void setUserphoneversion(String userphoneversion) {
        this.userphoneversion = userphoneversion == null ? null : userphoneversion.trim();
    }

    public String getUsermac() {
        return usermac;
    }

    public void setUsermac(String usermac) {
        this.usermac = usermac == null ? null : usermac.trim();
    }

    public String getUserip() {
        return userip;
    }

    public void setUserip(String userip) {
        this.userip = userip == null ? null : userip.trim();
    }

    public Integer getUsertype() {
        return usertype;
    }

    public void setUsertype(Integer usertype) {
        this.usertype = usertype;
    }
}