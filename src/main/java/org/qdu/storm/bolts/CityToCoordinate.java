package org.qdu.storm.bolts;

import javafx.util.Pair;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.qdu.storm.Mapping.AreaToCoordinate;

import java.util.Map;

public class CityToCoordinate extends BaseRichBolt {

    OutputCollector collector;
    String city;
    AreaToCoordinate atc;
    Pair<Double,Double> res;

    @Override
    public void prepare(Map<String, Object> map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
        atc = new AreaToCoordinate();
    }

    @Override
    public void execute(Tuple tuple) {
        city = tuple.getStringByField("city");
        res = Get(city);

        if(res.getKey() != 0.0 && res.getValue() != 0.0){
            this.collector.emit(new Values(res.getKey(),res.getValue()));
        }
    }

    Pair<Double,Double> Get(String ct){
        Pair<Double,Double> p;
        for (Map.Entry<String,Pair<Double,Double>> entry : atc.coordinate.entrySet()) {

            if (ct.contains(entry.getKey())){
                p = new Pair<>(entry.getValue().getKey(),entry.getValue().getValue());
                return p;
            }
        }
        p = new Pair<>(0.0,0.0);
        return p;
    }


    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("longitude","latitude"));
    }
}
