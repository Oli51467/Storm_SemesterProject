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
    将一个城市所带的后缀去除
 */
public class TrimSuffix extends BaseRichBolt {

    OutputCollector collector;
    String city;

    @Override
    public void prepare(Map<String, Object> map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        city = tuple.getStringByField("city");
        //如果有特定后缀，就去掉
        if(check(city)){
            city = city.substring(0,city.length()-1);
        }
        //否则什么也不做
        collector.emit(tuple,new Values(city));
        collector.ack(tuple);
    }

    boolean check(String str){
        char c = str.charAt(str.length()-1);
        if(c == '市' || c == '区' || c == '州' || c == '县' ) return true;
        else return false;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("trimcity"));
    }
}
