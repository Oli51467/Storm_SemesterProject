package org.qdu.kafka.newstyle;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.storm.tuple.Values;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Properties;
import java.util.Random;

public class Producer {
    public static String topic = "test";//定义主题

    public static void main(String[] args) throws InterruptedException {
        Properties p = new Properties();
        p.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop-master:9092");//kafka地址，多个地址用逗号分割
        p.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        p.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        KafkaProducer<String,String> kafkaProducer = new KafkaProducer<>(p);

        FileReader fileReader;
        int id=0;
        try {
            while (true) {
                try {
                    fileReader = new FileReader("D:/Storm/app.log");
                } catch (
                        FileNotFoundException e) {
                    throw new RuntimeException("Error reading file app.log");
                }
                String line;
                //Open the reader
                BufferedReader reader = new BufferedReader(fileReader);
                try{
                    //读取每一行
                    while((line = reader.readLine()) != null){
                        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic,line);
                        kafkaProducer.send(record);
                        System.out.println("消息发送成功了吗..."+id++);
                        Thread.sleep(1000);
                    }
                }catch(Exception e){
                    throw new RuntimeException("Error reading tuple",e);
                }
                //String msg = "Hello," + new Random().nextInt(100);
                //
                //kafkaProducer.send(record);
                //System.out.println("消息发送成功:" + msg);
                //Thread.sleep(1500);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            kafkaProducer.close();
        }
    }
}
