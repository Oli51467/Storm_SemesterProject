package org.qdu.storm.bolts;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

public class eval extends BaseRichBolt {

    OutputCollector collector;
    String city;
    Double lng,lat;
    int value;

    @Override
    public void prepare(Map<String, Object> map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        city = tuple.getStringByField("CITY");
        lng = tuple.getDoubleByField("longitude");
        lat = tuple.getDoubleByField("latitude");
        value = check(city);
        collector.emit(tuple,new Values(lng,lat,city,value));
        collector.ack(tuple);
    }

    int check(String city){
        char c = city.charAt(city.length()-1);
        if(c == '市') return 15;
        else return 10;
    }
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("lng","lat","CITY","value"));
    }
}
