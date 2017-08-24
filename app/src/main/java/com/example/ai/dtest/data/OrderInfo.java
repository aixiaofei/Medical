package com.example.ai.dtest.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by ai on 2017/8/23.
 */

public class OrderInfo implements Serializable{
    private int userorderid;

    private String userorderptime;

    private String userorderrtime;

    private String userorderetime;

    private int docloginid;

    private String docname;

    private String hospname;

    private int userorderstate;

    public int getUserorderid() {
        return userorderid;
    }

    public void setUserorderid(int userorderid) {
        this.userorderid = userorderid;
    }

    public String getUserorderptime() {
        return userorderptime;
    }

    public void setUserorderptime(String userorderptime) {
        this.userorderptime = userorderptime;
    }

    public String getUserorderrtime() {
        return userorderrtime;
    }

    public void setUserorderrtime(String userorderrtime) {
        this.userorderrtime = userorderrtime;
    }

    public String getUserorderetime() {
        return userorderetime;
    }

    public void setUserorderetime(String userorderetime) {
        this.userorderetime = userorderetime;
    }

    public int getDocloginid() {
        return docloginid;
    }

    public void setDocloginid(int docloginid) {
        this.docloginid = docloginid;
    }

    public String getDocname() {
        return docname;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public String getHospname() {
        return hospname;
    }

    public void setHospname(String hospname) {
        this.hospname = hospname;
    }

    public int getUserorderstate() {
        return userorderstate;
    }

    public void setUserorderstate(int userorderstate) {
        this.userorderstate = userorderstate;
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
