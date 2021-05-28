package org.qdu.storm.bolts;

import javafx.util.Pair;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.trident.operation.builtin.Min;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.qdu.storm.Mapping.IpToArea;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

//9位或10位的ipaddr转化成地区
public class AddrToCity extends BaseRichBolt {

    OutputCollector collector;
    Long addr;
    String city;
    IpToArea ToArea;

    @Override
    public void prepare(Map<String, Object> conf, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
        ToArea = new IpToArea();
    }

    @Override
    public void execute(Tuple tuple) {
        addr = tuple.getLongByField("ipaddr");

        city = Get(addr);
        if(city != ""){
            collector.emit(new Values(city));
        }
    }

    String Get(Long ar){
        for (Map.Entry<Pair<Long,Long>,String> entry : ToArea.region.entrySet()) {
            if(ar >= entry.getKey().getKey() && ar <= entry.getKey().getValue()){
                return entry.getValue();
            }
            //System.out.println("Key = " + entry.getKey().getKey() + ", Value = " + entry.getValue());
        }
        return "";
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("city"));
    }
}
