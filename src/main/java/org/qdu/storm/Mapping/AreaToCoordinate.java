package org.qdu.storm.Mapping;

import javafx.util.Pair;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

/*
    从HDFS中读取，将地区与坐标的映射存入哈希表中
    以提高bolts中信息转化的速度
 */
public class AreaToCoordinate {

    //hash
    public HashMap<String, Pair<Double, Double>> coordinate = new HashMap<>();
    //一些从HDFS读数据的流
    FSDataInputStream fsr = null;
    BufferedReader bufferedReader = null;
    String lines;
    String[] fields;
    String region;
    double longitude, latitude;
    int idx = 0;

    public AreaToCoordinate() {
        try {
            FileSystem fs = FileSystem.get(new URI("hdfs://hadoop-master:9000"), new Configuration());
            fsr = fs.open(new Path("/data/lng-lat-mapping.txt"));
            bufferedReader = new BufferedReader(new InputStreamReader(fsr));

            while ((lines = bufferedReader.readLine()) != null) {
                //文件以逗号分隔
                fields = lines.split(",");

                //将地区，经纬度提取出来
                longitude = Double.parseDouble(fields[2]);
                latitude = Double.parseDouble(fields[3]);
                region = fields[1];

                //存入hash表
                Pair<Double, Double> p = new Pair<>(longitude, latitude);
                coordinate.put(region, p);
                idx++;
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //测试
    /*public static void main(String[] args) {
        AreaToCoordinate a = new AreaToCoordinate();

        for (Map.Entry<String,Pair<Double,Double>> entry : a.coordinate.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value =" + entry.getValue());
        }
        System.out.println(a.idx);
    }*/
}
