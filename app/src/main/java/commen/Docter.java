package commen;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;

/**
 * Created by ai on 2017/7/9.
 */

public class Docter implements Serializable {

    private int id;

    private String name;

    private int age;

//    private int imageId;

    private String keshi;

    private String hospital;

    private String specialty;

    public Docter(int id,String name,int age,String keshi,String hospital,String specialty){
        this.id=id;
        this.name=name;
        this.age=age;
//        this.imageId=imageId;
        this.keshi=keshi;
        this.hospital=hospital;
        this.specialty=specialty;
    }

    public int getId(){
        return  id;
    }

    public String getName(){
        return name;
    }

    public int getAge(){
        return age;
    }

//    public int getImageId(){
//        return imageId;
//    }

    public String getKeshi(){
        return keshi;
    }

    public String getHospital(){
        return hospital;
    }

    public String getSpecialty(){
        return specialty;
    }

    public Object deepClone() throws IOException,ClassNotFoundException{//将对象写到流里
        ByteArrayOutputStream bo=new ByteArrayOutputStream();
        ObjectOutputStream oo=new ObjectOutputStream(bo);
        oo.writeObject(this);//从流里读出来
        ByteArrayInputStream bi=new ByteArrayInputStream(bo.toByteArray());
        ObjectInputStream oi=new ObjectInputStream(bi);
        return(oi.readObject());
    }
}
