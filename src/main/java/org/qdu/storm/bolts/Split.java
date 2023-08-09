package org.qdu.storm.bolts;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

/*
    将句子切分
 */
public class Split extends BaseRichBolt {

    private OutputCollector collector;

    @Override
    public void prepare(Map<String, Object> map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
    }

    /*
        从kafkaSpout(AppLogsProducer)中获取
     */
    @Override
    public void execute(Tuple tuple) {
        //System.out.println(tuple.getStringByField("value"));
        String lines = tuple.getStringByField("value");
        String[] fields = lines.split("\t");
        String ipConf = fields[3].trim();
        collector.emit(tuple, new Values(ipConf));
        collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("ipconf"));
    }
}
