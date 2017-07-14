package commen;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

public class TDoctorinfo {
    private Integer docid;

    private String docname;

    private String docmale;

    private Integer docage;

    private String docidnum;

    private String docidphoto;

    private Date docbirthdate;

    private String docnation;

    private Integer hospitalid;

    private String docdepartment;

    private String docqualificationpho;

    private String docworkcardpho;

    private String doctrainingpho;

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

    public String getDocidnum() {
        return docidnum;
    }

    public void setDocidnum(String docidnum) {
        this.docidnum = docidnum == null ? null : docidnum.trim();
    }

    public String getDocidphoto() {
        return docidphoto;
    }

    public void setDocidphoto(String docidphoto) {
        this.docidphoto = docidphoto == null ? null : docidphoto.trim();
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

    public Integer getHospitalid() {
        return hospitalid;
    }

    public void setHospitalid(Integer hospitalid) {
        this.hospitalid = hospitalid;
    }

    public String getDocdepartment() {
        return docdepartment;
    }

    public void setDocdepartment(String docdepartment) {
        this.docdepartment = docdepartment == null ? null : docdepartment.trim();
    }

    public String getDocqualificationpho() {
        return docqualificationpho;
    }

    public void setDocqualificationpho(String docqualificationpho) {
        this.docqualificationpho = docqualificationpho == null ? null : docqualificationpho.trim();
    }

    public String getDocworkcardpho() {
        return docworkcardpho;
    }

    public void setDocworkcardpho(String docworkcardpho) {
        this.docworkcardpho = docworkcardpho == null ? null : docworkcardpho.trim();
    }

    public String getDoctrainingpho() {
        return doctrainingpho;
    }

    public void setDoctrainingpho(String doctrainingpho) {
        this.doctrainingpho = doctrainingpho == null ? null : doctrainingpho.trim();
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
    public Object deepCopy() throws IOException, ClassNotFoundException{
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