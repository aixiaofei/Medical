package com.example.ai.dtest.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by ai on 2017/8/25.
 */

public class OrderDetailInfo implements Serializable{
    private int userorderid;
    private int userorderhprice;
    private int userorderdprice;
    private String hospname;
    private String userorderstime;

    public int getUserorderid() {
        return userorderid;
    }

    public void setUserorderid(int userorderid) {
        this.userorderid = userorderid;
    }

    public int getUserorderhprice() {
        return userorderhprice;
    }

    public void setUserorderhprice(int userorderhprice) {
        this.userorderhprice = userorderhprice;
    }

    public int getUserorderdprice() {
        return userorderdprice;
    }

    public void setUserorderdprice(int userorderdprice) {
        this.userorderdprice = userorderdprice;
    }

    public String getHospname() {
        return hospname;
    }

    public void setHospname(String hospname) {
        this.hospname = hospname;
    }

    public String getUserorderstime() {
        return userorderstime;
    }

    public void setUserorderstime(String userorderstime) {
        this.userorderstime = userorderstime;
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
