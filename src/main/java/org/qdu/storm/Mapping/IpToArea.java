package org.qdu.storm.Mapping;

import javafx.util.Pair;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/*
    从HDFS中读取，将IP与地区的映射存入哈希表中
    以提高bolts中信息转化的速度
    其中，IP是最小IP和最大IP的范围
 */
public class IpToArea {

    // hash
    public HashMap<Pair<Long, Long>, String> region = new HashMap<>();

    // 一些从HDFS读数据的流
    FSDataInputStream fsr = null;
    BufferedReader bufferedReader = null;
    String lines = null;
    String[] fields;
    Long MinIp, maxIp;
    int idx = 0;

    public IpToArea() {
        try {
            FileSystem fs = FileSystem.get(new URI("hdfs://hadoop-master:9000"), new Configuration());
            fsr = fs.open(new Path("/data/ip_area_isp.txt"));
            bufferedReader = new BufferedReader(new InputStreamReader(fsr));

            while ((lines = bufferedReader.readLine()) != null) {
                fields = lines.split("\t");

                //剪掉不在中国的ip地址
                if (!fields[0].contains("中国")) continue;

                //最大ip和最小ip
                MinIp = Long.parseLong(fields[4]);
                maxIp = Long.parseLong(fields[5]);
                Pair<Long, Long> r = new Pair<>(MinIp, maxIp);

                if (!check(fields[2])) {
                    region.put(r, fields[2]);
                    idx++;
                }
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

    //如果地区的最小范围字段不包含如下四个后缀，则说明这个字段无效
    public boolean check(String fields) {
        return !fields.contains("市") && !fields.contains("区") && !fields.contains("州") && !fields.contains("县");
    }

    //测试
    /*public static void main(String[] args) {
        IpToArea a = new IpToArea();
        for (Map.Entry<Pair<Long, Long>, String> entry : a.region.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value =" + entry.getValue());
        }
        System.out.println(a.idx);
    }*/
}
