package com.example.ai.dtest.db;

import org.litepal.crud.DataSupport;

/**
 * Created by ai on 2017/8/5.
 */

public class City extends DataSupport {
    private String cityname;
    private String citycode;
    private String cityparentcode;
    private int citylevel;

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getCityparentcode() {
        return cityparentcode;
    }

    public void setCityparentcode(String cityparentcode) {
        this.cityparentcode = cityparentcode;
    }

    public int getCitylevel() {
        return citylevel;
    }

    public void setCitylevel(int citylevel) {
        this.citylevel = citylevel;
    }
}
