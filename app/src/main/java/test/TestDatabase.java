package test;

import android.util.Log;

import org.litepal.crud.DataSupport;

import java.util.List;

import commen.OffLineUser;

/**
 * Created by ai on 2017/7/11.
 */

public class TestDatabase {
    public static void main(String[] args){
        List<OffLineUser> offLineUsers= DataSupport.findAll(OffLineUser.class);
        for(OffLineUser i:offLineUsers){
            System.out.println(i.getUserName());
            System.out.println(i.getPassWord());
            System.out.println(i.getLastLoginTime());
            System.out.println(i.getIsAutoLogin());
        }
    }
}
