package org.qdu.kafka.newstyle;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;


public class KafkaConfig {
    public KafkaSpoutConfig KafkaSetting(String topic){
        KafkaSpoutConfig.Builder<String, String> kafkaSpoutConfigBuilder;
        //kafka连接信息
        String bootstrapServers = "hadoop-master:9092";
        //主题，首先需要保证kafka的队列中，下面的代码都已经执行过
        // kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
        // 程序启动以后，需要使用下属的脚本向Kafka队列发送数据
        // kafka-console-producer.sh --bootstrap-server bd-alone:9092 --topic test

        // 方式二：
        // KafkaConsumerProducerDemo和NewKafKaTopo可以同时运行，一个用于发射数据，一个用于接受数据
        //String topic = "test";
        /**
         * 构造kafkaSpoutConfigBuilder构造器
         *
         * bootstrapServers:    Kafka链接地址 ip:port
         * StringDeserializer:  key Deserializer    主题key的反序列化
         * StringDeserializer:  value Deserializer  主题的value的反序列化
         * topic: 主题名称
         */
        kafkaSpoutConfigBuilder = new KafkaSpoutConfig.Builder(
                bootstrapServers,
                topic);

        //使用kafkaSpoutConfigBuilder构造器构造kafkaSpoutConfig,并配置相应属性
        KafkaSpoutConfig<String, String> kafkaSpoutConfig = kafkaSpoutConfigBuilder
                /**
                 * 设置groupId
                 */
                .setProp(ConsumerConfig.GROUP_ID_CONFIG, topic.toLowerCase() + "_storm_group")

                /**
                 * 设置session超时时间,该值应介于
                 * [group.min.session.timeout.ms, group.max.session.timeout.ms] [6000,300000]
                 * 默认值:10000
                 */
                .setProp(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "100000")

                /**
                 * 设置拉取最大容量
                 */
                .setProp(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "1048576")

                /**
                 * 设置控制客户端等待请求响应的最大时间量
                 * 默认值:30000
                 */
                .setProp(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, "300000")

                /**
                 * 设置心跳到消费者协调器之间的预期时间。
                 * 心跳用于确保消费者的会话保持活动并且当新消费者加入或离开组时促进重新平衡
                 * 默认值:	3000        (一般设置低于session.timeout.ms的三分之一)
                 */
                .setProp(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "30000")

                /**
                 * 设置offset提交时间15s  默认30s
                 */
                .setOffsetCommitPeriodMs(15000)
                /**
                 * 设置配置消息的key/value的反序列化类
                 */
                .setProp("key.deserializer", StringDeserializer.class)
                .setProp("value.deserializer",StringDeserializer.class)

                /**
                 * 构造kafkaSpoutConfig
                 */
                .build();
                return kafkaSpoutConfig;
    }
}
