package org.qdu.storm.Mapping;

import javafx.util.Pair;
import java.io.*;
import java.util.*;

/*
    从文件中读取，将IP与地区的映射存入哈希表中
    以提高bolts中信息转化的速度
    其中，IP是最小IP和最大IP的范围
 */
public class IpToArea {

    //hash
    public HashMap<Pair<Long,Long>,String> region = new HashMap<>();
    public int idx=0;

    public IpToArea() {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader("D:/Storm/ip_area_isp.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error reading file");
        }

        BufferedReader reader = new BufferedReader(fileReader);

        String lines;
        String []fields;
        Long Minip,Maxip;
        try{
            while((lines = reader.readLine()) != null){
                fields = lines.split("\t");

                //剪掉不在中国的ip地址
                if(!fields[0].contains("中国")) continue;

                //最大ip和最小ip
                Minip = Long.parseLong(fields[4]);
                Maxip = Long.parseLong(fields[5]);
                Pair<Long,Long> r = new Pair<>(Minip,Maxip);

                if(check(fields[2])) continue;
                else {
                    region.put(r,fields[2]);
                }

                idx++;
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Error reading tuple", e);
        }
    }

    //如果地区的最小范围字段不包含如下四个后缀，则说明这个字段无效
    public boolean check(String fields){
        if(!fields.contains("市") && !fields.contains("区") && !fields.contains("州") && !fields.contains("县")) return true;
        else return false;
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
