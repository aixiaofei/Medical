package com.example.ai.dtest.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Usersick implements Serializable{

    private String phone;

    private String usersickid;

    private String usersickdesc;

    private String usersickpic;

    private String usersickdept;

    private String familyid;

    private Integer userorderid;

    private String familymale;

    private String familyname;

    private String familyage;

    private String usersicktime;

    private int usersickstateid;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFamliyid() {
        return familyid;
    }

    public void setFamliyid(String famliyid) {
        this.familyid = famliyid;
    }

    public String getUsersickid() {
        return usersickid;
    }

    public void setUsersickid(String usersickid) {
        this.usersickid = usersickid;
    }

    public String getUsersickdesc() {
        return usersickdesc;
    }

    public void setUsersickdesc(String usersickdesc) {
        this.usersickdesc = usersickdesc == null ? null : usersickdesc.trim();
    }

    public String getUsersickpic() {
        return usersickpic;
    }

    public void setUsersickpic(String usersickpic) {
        this.usersickpic = usersickpic == null ? null : usersickpic.trim();
    }

    public String getUsersickdept() {
        return usersickdept;
    }

    public void setUsersickdept(String usersickdept) {
        this.usersickdept = usersickdept == null ? null : usersickdept.trim();
    }

    public Integer getUserorderid() {
        return userorderid;
    }

    public void setUserorderid(Integer userorderid) {
        this.userorderid = userorderid;
    }


    public String getFamilymale() {
        return familymale;
    }

    public void setFamilymale(String familymale) {
        this.familymale = familymale;
    }

    public String getFamilyname() {
        return familyname;
    }

    public void setFamilyname(String familyname) {
        this.familyname = familyname;
    }

    public String getFamilyage() {
        return familyage;
    }

    public void setFamilyage(String familyage) {
        this.familyage = familyage;
    }

    public String getUsersicktime() {
        return usersicktime;
    }

    public void setUsersicktime(String usersicktime) {
        this.usersicktime = usersicktime;
    }

    public int getUsersickstateid() {
        return usersickstateid;
    }

    public void setUsersickstateid(int usersickstateid) {
        this.usersickstateid = usersickstateid;
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