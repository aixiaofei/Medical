package com.example.ai.dtest.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by ai on 2017/8/14.
 */

public class test2 {
    public static void main(String[] args){

        Map<Integer,Integer> a= new HashMap<>();
        List<Integer> a1= new ArrayList<>();

        Scanner in= new Scanner(System.in);
        int num= in.nextInt();
        int sum=0;
        if(num==1){
            System.out.println("1/1");
        }else if(num==2){
            System.out.println("1/1");
        }else {
            for (int i = 2; i < num; i++) {
                int p = num;
                while (p > 0) {
                    sum += p % i;
                    p = p / i;
                }
            }
            int m= sum;
            int n= num-2;
            while (m%n!=0){
                int temp=m;
                m=n;
                n=temp%n;
            }
            sum=sum/n;
            int wei= (num-2)/n;
            System.out.println(Integer.toString(sum)+"/"+Integer.toString(wei));
        }
    }
}
