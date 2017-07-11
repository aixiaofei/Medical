package test;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import commen.Docter;

/**
 * Created by ai on 2017/7/9.
 */

public class toDocterJson {
    public static void main(String[] args) {
        List<Docter> mList= new ArrayList<>();
        int i=1;
        while (i<=15){
            Docter docter1 = new Docter(i, "李四", 24, "外科", "安医大二附院", "脑科手术");
            mList.add(docter1);
            i+=1;
            Docter docter2 = new Docter(i, "陈武", 37, "内科", "安医大二附院", "肝功能");
            mList.add(docter2);
            i+=1;
            Docter docter3 = new Docter(i, "张三", 30, "外科", "安医大二附院", "骨折手术");
            mList.add(docter3);
            i+=1;
        }
        Gson gson= new Gson();
        String res= gson.toJson(mList);
        BufferedWriter writer=null;
        try{
            String filename= "E:\\Apache24\\htdocs\\data.json";
            writer= new BufferedWriter(new FileWriter(filename));
            writer.write(res);
        } catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if( writer != null){
                    writer.close();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
