package org.qdu.kafka;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Properties;


public class ApplogsProducer {
    public static String topic = "test1";//定义主题

    public static void main(String[] args) {
        Properties p = new Properties();
        p.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop-master:9092");//kafka地址，多个地址用逗号分割
        p.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        p.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        //Thread.currentThread().setContextClassLoader(null);
        KafkaProducer<String,String> kafkaProducer = new KafkaProducer<>(p);

        int id=0;
        FSDataInputStream fsr;
        BufferedReader bufferedReader;
        String lines;

        try {
            while (true) {
                try{
                    FileSystem fs = FileSystem.get(new URI("hdfs://hadoop-master:9000"),new Configuration());
                    fsr = fs.open(new Path("/data/app.log"));
                    bufferedReader = new BufferedReader(new InputStreamReader(fsr));

                    while((lines = bufferedReader.readLine())!=null){
                        ProducerRecord<String,String> record = new ProducerRecord<>(topic,lines);
                        kafkaProducer.send(record);
                        System.out.println("消息成功发送了吧..."+id++);
                        Thread.sleep(100);
                    }
                }catch(Exception e){
                    throw new RuntimeException("Error reading tuple",e);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            kafkaProducer.close();
        }
    }
}
