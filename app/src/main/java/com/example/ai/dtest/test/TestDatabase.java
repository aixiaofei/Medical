package com.example.ai.dtest.test;

import com.example.ai.dtest.db.OffLineUser;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by ai on 2017/7/11.
 */

public class TestDatabase {
    public static void main(String[] args){
        List<OffLineUser> offLineUsers= DataSupport.findAll(OffLineUser.class);
        for(OffLineUser i:offLineUsers){
            System.out.println(i.getUserPhone());
            System.out.println(i.getPassword());
            System.out.println(i.getLastLoginTime());
            System.out.println(i.getIsAutoLogin());
        }
    }
}
