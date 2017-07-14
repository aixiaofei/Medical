package commen;

import java.util.Date;

public class TUserlogininfo {
    private Integer userloginid;

    private String userloginusername;

    private String userloginpassword;

    private String userlogintoken;

    private Long userlogindeadline;

    private String userloginlongitude;

    private String userloginlatitude;

    private Date userloginlastlogintime;

    private String userloginlastloginlocation;

    private String userlogindevice;

    private String userloginbrowserversion;

    private String userloginphonemodel;

    private String userloginphoneversion;

    private String userloginmac;

    private String userloginip;

    private Integer userlogintype;

    private Integer uesrid;

    public Integer getUserloginid() {
        return userloginid;
    }

    public void setUserloginid(Integer userloginid) {
        this.userloginid = userloginid;
    }

    public String getUserloginusername() {
        return userloginusername;
    }

    public void setUserloginusername(String userloginusername) {
        this.userloginusername = userloginusername == null ? null : userloginusername.trim();
    }

    public String getUserloginpassword() {
        return userloginpassword;
    }

    public void setUserloginpassword(String userloginpassword) {
        this.userloginpassword = userloginpassword == null ? null : userloginpassword.trim();
    }

    public String getUserlogintoken() {
        return userlogintoken;
    }

    public void setUserlogintoken(String userlogintoken) {
        this.userlogintoken = userlogintoken == null ? null : userlogintoken.trim();
    }

    public Long getUserlogindeadline() {
        return userlogindeadline;
    }

    public void setUserlogindeadline(Long userlogindeadline) {
        this.userlogindeadline = userlogindeadline;
    }

    public String getUserloginlongitude() {
        return userloginlongitude;
    }

    public void setUserloginlongitude(String userloginlongitude) {
        this.userloginlongitude = userloginlongitude == null ? null : userloginlongitude.trim();
    }

    public String getUserloginlatitude() {
        return userloginlatitude;
    }

    public void setUserloginlatitude(String userloginlatitude) {
        this.userloginlatitude = userloginlatitude == null ? null : userloginlatitude.trim();
    }

    public Date getUserloginlastlogintime() {
        return userloginlastlogintime;
    }

    public void setUserloginlastlogintime(Date userloginlastlogintime) {
        this.userloginlastlogintime = userloginlastlogintime;
    }

    public String getUserloginlastloginlocation() {
        return userloginlastloginlocation;
    }

    public void setUserloginlastloginlocation(String userloginlastloginlocation) {
        this.userloginlastloginlocation = userloginlastloginlocation == null ? null : userloginlastloginlocation.trim();
    }

    public String getUserlogindevice() {
        return userlogindevice;
    }

    public void setUserlogindevice(String userlogindevice) {
        this.userlogindevice = userlogindevice == null ? null : userlogindevice.trim();
    }

    public String getUserloginbrowserversion() {
        return userloginbrowserversion;
    }

    public void setUserloginbrowserversion(String userloginbrowserversion) {
        this.userloginbrowserversion = userloginbrowserversion == null ? null : userloginbrowserversion.trim();
    }

    public String getUserloginphonemodel() {
        return userloginphonemodel;
    }

    public void setUserloginphonemodel(String userloginphonemodel) {
        this.userloginphonemodel = userloginphonemodel == null ? null : userloginphonemodel.trim();
    }

    public String getUserloginphoneversion() {
        return userloginphoneversion;
    }

    public void setUserloginphoneversion(String userloginphoneversion) {
        this.userloginphoneversion = userloginphoneversion == null ? null : userloginphoneversion.trim();
    }

    public String getUserloginmac() {
        return userloginmac;
    }

    public void setUserloginmac(String userloginmac) {
        this.userloginmac = userloginmac == null ? null : userloginmac.trim();
    }

    public String getUserloginip() {
        return userloginip;
    }

    public void setUserloginip(String userloginip) {
        this.userloginip = userloginip == null ? null : userloginip.trim();
    }

    public Integer getUserlogintype() {
        return userlogintype;
    }

    public void setUserlogintype(Integer userlogintype) {
        this.userlogintype = userlogintype;
    }

    public Integer getUesrid() {
        return uesrid;
    }

    public void setUesrid(Integer uesrid) {
        this.uesrid = uesrid;
    }
}