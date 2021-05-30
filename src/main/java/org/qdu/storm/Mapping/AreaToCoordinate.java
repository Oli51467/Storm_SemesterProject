package org.qdu.storm.Mapping;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

/*
    从文件中读取，将地区与坐标的映射存入哈希表中
    以提高bolts中信息转化的速度
 */
public class AreaToCoordinate {

    //hash
    public HashMap<String, Pair<Double,Double>> coordinate = new HashMap<>();

    public AreaToCoordinate(){
        FileReader fileReader = null;
        try {
            fileReader = new FileReader("D:/Storm/lng-lat-mapping.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error reading file");
        }

        BufferedReader reader = new BufferedReader(fileReader);

        String lines;
        String []fields;
        String region;
        double longitude,latitude;

        try {
            while((lines = reader.readLine())!=null){
                //文件以逗号分隔
                fields = lines.split(",");

                //将地区，经纬度提取出来
                longitude = Double.parseDouble(fields[2]);
                latitude = Double.parseDouble(fields[3]);
                region = fields[1];

                //存入hash表
                Pair<Double,Double> p = new Pair<>(longitude,latitude);
                coordinate.put(region,p);
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Error reading tuple", e);
        }
    }

    //测试
    /*public static void main(String[] args) {
        AreaToCoordinate a = new AreaToCoordinate();

        for (Map.Entry<String,Pair<Double,Double>> entry : a.coordinate.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value =" + entry.getValue());
        }
    }*/
}
