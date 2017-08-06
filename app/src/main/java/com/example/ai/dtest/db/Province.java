package com.example.ai.dtest.db;

import org.litepal.crud.DataSupport;

/**
 * Created by ai on 2017/8/5.
 */

public class Province extends DataSupport {
    private String name;
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
