package org.qdu.storm.bolts;

import javafx.util.Pair;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.qdu.storm.Mapping.IpToArea;
import java.util.Map;

/*
    将从上一个bolt接收的ip地址转化成地区
    通过Toarea获取哈希表
 */
public class AddrToCity extends BaseRichBolt {

    OutputCollector collector;
    Long addr;
    String city;
    IpToArea ToArea;

    @Override
    public void prepare(Map<String, Object> conf, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
        //初始化hash表
        ToArea = new IpToArea();
    }

    @Override
    public void execute(Tuple tuple) {
        addr = tuple.getLongByField("ipaddr");

        //查表
        city = Get(addr);
        //如果有映射，就发射到下一个bolt中
        if(city != ""){
            collector.emit(tuple,new Values(city));
            collector.ack(tuple);
        }

    }

    String Get(Long ar){
        for (Map.Entry<Pair<Long,Long>,String> entry : ToArea.region.entrySet()) {
            if(ar >= entry.getKey().getKey() && ar <= entry.getKey().getValue()){
                return entry.getValue();
            }
        }
        return "";
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("city"));
    }
}
