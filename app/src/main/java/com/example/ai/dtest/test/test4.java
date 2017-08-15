package com.example.ai.dtest.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Created by ai on 2017/8/14.
 */

public class test4 {
    public static void main(String[] args){
        List<Integer> s = new ArrayList<>();
        Scanner in = new Scanner(System.in);
        int j = 0;
        String l1 = null;
        String l2 = null;
        while (j < 2) {
            String buf = in.nextLine();
            if (!buf.equals("")) {
                if(j==0) {
                    l1 = buf;
                }else {
                    l2=buf;
                }
                j++;
            }
        }
        int M = Integer.parseInt(l1.split(" ")[1]);
        for (String num : l2.split(" ")) {
            s.add(Integer.parseInt(num));
        }
        int i = 0;
        while (i < M) {
            String buf = in.nextLine();
            if (!buf.equals("")) {
                String[] l = buf.split(" ");
                int m = Integer.parseInt(l[1]);
                int n = Integer.parseInt(l[2]);
                if (l[0].charAt(0) == 'Q') {
                    if (m > n) {
                        int temp = m;
                        m = n;
                        n = temp;
                    }
                    List<Integer> subList = s.subList(m - 1, n);
                    System.out.println(Collections.max(subList));
                } else {
                    s.set(m - 1, n);
                }
                i++;
            }
        }
    }
}
