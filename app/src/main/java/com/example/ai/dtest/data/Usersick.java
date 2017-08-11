package com.example.ai.dtest.data;

public class Usersick {

    private String phone;

    private Integer usersickid;

    private String usersickdesc;

    private String usersickpic;

    private String usersickdept;

    private String famliyid;

    private Integer userorderid;

    private Integer userloginid;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFamliyid() {
        return famliyid;
    }

    public void setFamliyid(String famliyid) {
        this.famliyid = famliyid;
    }

    public Integer getUsersickid() {
        return usersickid;
    }

    public void setUsersickid(Integer usersickid) {
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

    public Integer getUserloginid() {
        return userloginid;
    }

    public void setUserloginid(Integer userloginid) {
        this.userloginid = userloginid;
    }
}