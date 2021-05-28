package org.qdu.storm.bolts;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

public class TestOfPrint extends BaseRichBolt {

    OutputCollector collector;
    double longti,lati;

    @Override
    public void prepare(Map<String, Object> map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector=outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        longti = tuple.getDoubleByField("longitude");
        lati = tuple.getDoubleByField("latitude");

        System.out.println("转化后的经度为:   "+longti);
        System.out.println("转化后的纬度为:   "+lati);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
