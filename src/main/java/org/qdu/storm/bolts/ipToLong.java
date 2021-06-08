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
    将一个IP地址通过转化算法转化成一个Long型整数
 */
public class ipToLong extends BaseRichBolt {

    OutputCollector collector;
    String rec;
    Long ipaddr;

    @Override
    public void prepare(Map<String, Object> map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector=outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        rec = tuple.getStringByField("ipconf");

        ipaddr = iptolong(rec);

        collector.emit(tuple,new Values(ipaddr));
        collector.ack(tuple);
    }

    //转化算法
    public static long iptolong(String strIp){
        long[] ip = new long[4];

        int pos1=strIp.indexOf(".");
        int pos2=strIp.indexOf(".",pos1+1);
        int pos3=strIp.indexOf(".",pos2+1);

        ip[0] = Long.parseLong(strIp.substring(0,pos1));
        ip[1] = Long.parseLong(strIp.substring(pos1+1,pos2));
        ip[2] = Long.parseLong(strIp.substring(pos2+1,pos3));
        ip[3] = Long.parseLong(strIp.substring(pos3+1));

        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("ipaddr"));
    }
}
