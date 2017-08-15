package com.example.ai.dtest.test;

import java.util.Date;
import java.util.Scanner;

/**
 * Created by ai on 2017/8/14.
 */

public class test3 {
    public static void main(String[] args){
        Scanner in= new Scanner(System.in);
        int num= in.nextInt();
        Date date= new Date();
        long time1= date.getTime();
        int count=0;
        for(int i=1;i<=num;i++){
            int p=i;
            int sum1=0;
            int sum2=0;
            while (p>0){
                sum1+=p%10;
                p/=10;
            }
            p=i;
            while (p>0){
                sum2+=p%2;
                p/=2;
            }
            if(sum1==sum2){
                count++;
            }
        }
        Date date1= new Date();
        System.out.println(date1.getTime()-time1);
        System.out.println(count);
    }
}
