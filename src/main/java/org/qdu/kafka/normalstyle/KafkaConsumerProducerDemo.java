package org.qdu.kafka.normalstyle;

public class KafkaConsumerProducerDemo {
    public static void main(String[] args)
    {
        KafkaProducer producerThread = new KafkaProducer(KafkaProperties.topic);
        producerThread.start();
//        KafkaConsumer consumerThread = new KafkaConsumer(KafkaProperties.topic);
//        consumerThread.start();
    }
}
