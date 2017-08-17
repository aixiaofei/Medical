package com.example.ai.dtest.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

public class DoctorCustom implements Serializable{

    private Integer docid;

    private String docname;

    private String docmale;

    private Integer docage;

    private Integer doctitleid;

    private String doccardnum;

    private String doccardphoto;

    private Date docbirthdate;

    private String docnation;

    private String dochosp;

    private String docdept;

    private String doctitlephoto;

    private String docworkcardphoto;

    private String doctrainphoto;

    private String docexpert;

    private String docotherphoto;

    private Boolean docallday;

    private String docabs;

    private String docloc;

    private String doclat;

    private String doclon;

    private Integer docloginid;

    private String distance;

    private String doctitlename;

    private String hosplevelname;

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

    public Integer getDoctitleid() {
        return doctitleid;
    }

    public void setDoctitleid(Integer doctitleid) {
        this.doctitleid = doctitleid;
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

    public String getDochosp() {
        return dochosp;
    }

    public void setDochosp(String dochosp) {
        this.dochosp = dochosp == null ? null : dochosp.trim();
    }

    public String getDocdept() {
        return docdept;
    }

    public void setDocdept(String docdept) {
        this.docdept = docdept == null ? null : docdept.trim();
    }

    public String getDoctitlephoto() {
        return doctitlephoto;
    }

    public void setDoctitlephoto(String doctitlephoto) {
        this.doctitlephoto = doctitlephoto == null ? null : doctitlephoto.trim();
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

    public Boolean getDocallday() {
        return docallday;
    }

    public void setDocallday(Boolean docallday) {
        this.docallday = docallday;
    }

    public String getDocabs() {
        return docabs;
    }

    public void setDocabs(String docabs) {
        this.docabs = docabs == null ? null : docabs.trim();
    }

    public String getDocloc() {
        return docloc;
    }

    public void setDocloc(String docloc) {
        this.docloc = docloc == null ? null : docloc.trim();
    }

    public String getDoclat() {
        return doclat;
    }

    public void setDoclat(String doclat) {
        this.doclat = doclat == null ? null : doclat.trim();
    }

    public String getDoclon() {
        return doclon;
    }

    public void setDoclon(String doclon) {
        this.doclon = doclon == null ? null : doclon.trim();
    }

    public Integer getDocloginid() {
        return docloginid;
    }

    public String getDocdistance() {
        return distance;
    }

    public void setDocdistance(String docdistance) {
        this.distance = docdistance;
    }

    public String getDoctitlename() {
        return doctitlename;
    }

    public void setDoctitlename(String doctitlename) {
        this.doctitlename = doctitlename;
    }

    public void setDocloginid(Integer docloginid) {
        this.docloginid = docloginid;
    }

    public String getHosplevelname() {
        return hosplevelname;
    }

    public void setHosplevelname(String hosplevelname) {
        this.hosplevelname = hosplevelname;
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