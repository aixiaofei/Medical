package com.example.ai.dtest.commen;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

public class Doctorinfo {
    private Integer docid;

    private String docname;

    private String docmale;

    private Integer docage;

    private String doccardnum;

    private String doccardphoto;

    private Date docbirthdate;

    private String docnation;

    private Integer hospid;

    private String docdept;

    private String docqualifphoto;

    private String docworkcardphoto;

    private String doctrainphoto;

    private String docexpert;

    private String docotherphoto;

    public Integer getDocid() {
        return docid;
    }

    public void setDocid(Integer docid) {
        this.docid = docid;
    }

    public String getDocname() {
        return docname;
    }

    public void setDocname(String docname) {
        this.docname = docname == null ? null : docname.trim();
    }

    public String getDocmale() {
        return docmale;
    }

    public void setDocmale(String docmale) {
        this.docmale = docmale == null ? null : docmale.trim();
    }

    public Integer getDocage() {
        return docage;
    }

    public void setDocage(Integer docage) {
        this.docage = docage;
    }

    public String getDoccardnum() {
        return doccardnum;
    }

    public void setDoccardnum(String doccardnum) {
        this.doccardnum = doccardnum == null ? null : doccardnum.trim();
    }

    public String getDoccardphoto() {
        return doccardphoto;
    }

    public void setDoccardphoto(String doccardphoto) {
        this.doccardphoto = doccardphoto == null ? null : doccardphoto.trim();
    }

    public Date getDocbirthdate() {
        return docbirthdate;
    }

    public void setDocbirthdate(Date docbirthdate) {
        this.docbirthdate = docbirthdate;
    }

    public String getDocnation() {
        return docnation;
    }

    public void setDocnation(String docnation) {
        this.docnation = docnation == null ? null : docnation.trim();
    }

    public Integer getHospid() {
        return hospid;
    }

    public void setHospid(Integer hospid) {
        this.hospid = hospid;
    }

    public String getDocdept() {
        return docdept;
    }

    public void setDocdept(String docdept) {
        this.docdept = docdept == null ? null : docdept.trim();
    }

    public String getDocqualifphoto() {
        return docqualifphoto;
    }

    public void setDocqualifphoto(String docqualifphoto) {
        this.docqualifphoto = docqualifphoto == null ? null : docqualifphoto.trim();
    }

    public String getDocworkcardphoto() {
        return docworkcardphoto;
    }

    public void setDocworkcardphoto(String docworkcardphoto) {
        this.docworkcardphoto = docworkcardphoto == null ? null : docworkcardphoto.trim();
    }

    public String getDoctrainphoto() {
        return doctrainphoto;
    }

    public void setDoctrainphoto(String doctrainphoto) {
        this.doctrainphoto = doctrainphoto == null ? null : doctrainphoto.trim();
    }

    public String getDocexpert() {
        return docexpert;
    }

    public void setDocexpert(String docexpert) {
        this.docexpert = docexpert == null ? null : docexpert.trim();
    }

    public String getDocotherphoto() {
        return docotherphoto;
    }

    public void setDocotherphoto(String docotherphoto) {
        this.docotherphoto = docotherphoto == null ? null : docotherphoto.trim();
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