package com.example.ai.dtest.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by ai on 2017/8/3.
 */

public class FamilyInfo implements Serializable{
    private String phone;
    private String userloginid;
    private String familyid;
    private String familyname;
    private String familymale;
    private String familyage;


    public String getFamilyid() {
        return familyid;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserloginid() {
        return userloginid;
    }

    public void setUserloginid(String userloginid) {
        this.userloginid = userloginid;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFamilyid(String familyid) {
        this.familyid = familyid;
    }

    public String getFamilyname() {
        return familyname;
    }

    public void setFamilyname(String familyname) {
        this.familyname = familyname;
    }

    public String getFamilymale() {
        return familymale;
    }

    public void setFamilymale(String familymale) {
        this.familymale = familymale;
    }

    public String getFamilyage() {
        return familyage;
    }

    public void setFamilyage(String familyage) {
        this.familyage = familyage;
    }

    public Object deepCopy() throws IOException, ClassNotFoundException {
        //字节数组输出流，暂存到内存中
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //序列化
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        //反序列化
        return ois.readObject();
    }
}
