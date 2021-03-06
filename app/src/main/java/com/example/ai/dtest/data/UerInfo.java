package com.example.ai.dtest.data;

import java.io.Serializable;
import java.util.Date;

public class UerInfo implements Serializable{

    private Integer userid;

    private String username;

    private String usermale;

    private String userage;

    private String usercardnum;

    private String usercardphoto;

    private Date userbirthdate;

    private String useradr;

    private Boolean usermarry;

    private String usernation;

    private String usercareer;

    private String userallergy;

    private String userdisease;

    private String useroperate;

    private String userdegree;

    private String userphone;

    private String useremail;

    private String usercollectdoc;

    private String userrelation;

    private Integer userloginid;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getUsermale() {
        return usermale;
    }

    public void setUsermale(String usermale) {
        this.usermale = usermale == null ? null : usermale.trim();
    }

    public String getUserage() {
        return userage;
    }

    public void setUserage(String userage) {
        this.userage = userage;
    }

    public String getUsercardnum() {
        return usercardnum;
    }

    public void setUsercardnum(String usercardnum) {
        this.usercardnum = usercardnum == null ? null : usercardnum.trim();
    }

    public String getUsercardphoto() {
        return usercardphoto;
    }

    public void setUsercardphoto(String usercardphoto) {
        this.usercardphoto = usercardphoto == null ? null : usercardphoto.trim();
    }

    public Date getUserbirthdate() {
        return userbirthdate;
    }

    public void setUserbirthdate(Date userbirthdate) {
        this.userbirthdate = userbirthdate;
    }

    public String getUseradr() {
        return useradr;
    }

    public void setUseradr(String useradr) {
        this.useradr = useradr == null ? null : useradr.trim();
    }

    public Boolean getUsermarry() {
        return usermarry;
    }

    public void setUsermarry(Boolean usermarry) {
        this.usermarry = usermarry;
    }

    public String getUsernation() {
        return usernation;
    }

    public void setUsernation(String usernation) {
        this.usernation = usernation == null ? null : usernation.trim();
    }

    public String getUsercareer() {
        return usercareer;
    }

    public void setUsercareer(String usercareer) {
        this.usercareer = usercareer == null ? null : usercareer.trim();
    }

    public String getUserallergy() {
        return userallergy;
    }

    public void setUserallergy(String userallergy) {
        this.userallergy = userallergy == null ? null : userallergy.trim();
    }

    public String getUserdisease() {
        return userdisease;
    }

    public void setUserdisease(String userdisease) {
        this.userdisease = userdisease == null ? null : userdisease.trim();
    }

    public String getUseroperate() {
        return useroperate;
    }

    public void setUseroperate(String useroperate) {
        this.useroperate = useroperate == null ? null : useroperate.trim();
    }

    public String getUserdegree() {
        return userdegree;
    }

    public void setUserdegree(String userdegree) {
        this.userdegree = userdegree == null ? null : userdegree.trim();
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone == null ? null : userphone.trim();
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail == null ? null : useremail.trim();
    }

    public String getUsercollectdoc() {
        return usercollectdoc;
    }

    public void setUsercollectdoc(String usercollectdoc) {
        this.usercollectdoc = usercollectdoc == null ? null : usercollectdoc.trim();
    }

    public String getUserrelation() {
        return userrelation;
    }

    public void setUserrelation(String userrelation) {
        this.userrelation = userrelation == null ? null : userrelation.trim();
    }

    public Integer getUserloginid() {
        return userloginid;
    }

    public void setUserloginid(Integer userloginid) {
        this.userloginid = userloginid;
    }
}