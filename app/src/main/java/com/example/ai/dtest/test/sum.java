package com.example.ai.dtest.test;

import android.util.ArraySet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ai on 2017/8/10.
 */

public class sum {

    public static void main(String[] args){
        int[] m= {1,2,4,5,3};

        int sum=0;

        Map<Integer,Set<Integer>> p= new HashMap<>();

        for(int i=0;i<m.length;i++){

            int j = i - 1;
            if(j==-1){
                j=(j+m.length)%(m.length);
            }
            while (m[j] <= m[i] && j!=i) {
                if(p.containsKey(j) && p.get(j).contains(i)){
                    sum+=1;
                    p.get(j).remove(i);
                }else if(!p.containsKey(i)){
                    Set<Integer> buf= new HashSet<>();
                    buf.add(j);
                    p.put(i,buf);
                }else {
                    p.get(i).add(j);
                }
                j -= 1;
                if(j==-1){
                    j=(j+m.length)%(m.length);
                }
            }
            if(j!=i) {
                if (p.containsKey(j) && p.get(j).contains(i)) {
                    sum += 1;
                    p.get(j).remove(i);
                } else if (!p.containsKey(i)) {
                    Set<Integer> buf = new HashSet<>();
                    buf.add(j);
                    p.put(i, buf);
                } else {
                    p.get(i).add(j);
                }
            }



            j = i + 1;
            if(j==m.length){
                j=(j-m.length)%m.length;
            }
            while (m[j] <= m[i] && j!=i) {
                if(p.containsKey(j) && p.get(j).contains(i)){
                    sum+=1;
                    p.get(j).remove(i);
                }else if(!p.containsKey(i)){
                    Set<Integer> buf= new HashSet<>();
                    buf.add(j);
                    p.put(i,buf);
                }else {
                    p.get(i).add(j);
                }
                j += 1;
                if(j==m.length){
                    j=(j-m.length)%m.length;
                }
            }
            if(j!=i) {
                if (p.containsKey(j) && p.get(j).contains(i)) {
                    sum += 1;
                    p.get(j).remove(i);
                } else if (!p.containsKey(i)) {
                    Set<Integer> buf = new HashSet<>();
                    buf.add(j);
                    p.put(i, buf);
                } else {
                    p.get(i).add(j);
                }
            }


            System.out.println(sum);
        }

        System.out.println(sum);
    }
}
