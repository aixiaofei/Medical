package com.example.ai.dtest.test;


import com.example.ai.dtest.commen.SimpleCrypto;

/**
 * Created by ai on 2017/7/11.
 */

public class AES {
    public static void main(String[] args){
        String seed= "send";
        String origin= "dwwfwfwf";
        String encrypt= null;
        String decrypt=null;
        try {
            encrypt= SimpleCrypto.encrypt(seed,origin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            decrypt= SimpleCrypto.decrypt(seed,encrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(decrypt.equals(origin)){
            System.out.print(origin);
            System.out.println(decrypt);
        }
    }
}
