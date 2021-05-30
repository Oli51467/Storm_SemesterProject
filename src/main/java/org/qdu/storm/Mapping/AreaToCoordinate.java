package org.qdu.storm.Mapping;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

public class AreaToCoordinate {

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
                fields = lines.split(",");

                longitude = Double.parseDouble(fields[2]);
                latitude = Double.parseDouble(fields[3]);
                region = fields[1];

                Pair<Double,Double> p = new Pair<>(longitude,latitude);
                coordinate.put(region,p);
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Error reading tuple", e);
        }
    }

    /*public static void main(String[] args) {
        AreaToCoordinate a = new AreaToCoordinate();

        for (Map.Entry<String,Pair<Double,Double>> entry : a.coordinate.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value =" + entry.getValue());
        }
    }*/
}
