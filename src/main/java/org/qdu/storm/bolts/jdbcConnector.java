package org.qdu.storm.bolts;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.qdu.storm.jdbcUtils.JDBCStateConfig;
import org.qdu.storm.jdbcUtils.JDBCUtil;

import java.sql.SQLException;
import java.util.Map;

public class jdbcConnector extends BaseRichBolt {

    OutputCollector collector;
    double longti;
    double lati;
    JDBCStateConfig jdbconfig;
    JDBCUtil jdbcUtil;
    String tablename;


    @Override
    public void prepare(Map<String, Object> map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
        setting();
    }

    @Override
    public void execute(Tuple tuple) {
        longti = tuple.getDoubleByField("longitude");
        lati = tuple.getDoubleByField("latitude");

        tablename = jdbconfig.getTable();
        String sql = "insert into " + tablename +" (longitude,latitude) values(" +longti+","+lati+");";
        jdbcUtil.insert(sql);
        collector.emit(new Values("success"));
    }

    void setting(){
        jdbconfig = new JDBCStateConfig();
        jdbconfig.setDriver("com.mysql.jdbc.Driver");
        jdbconfig.setTable("Location");
        jdbconfig.setUrl("jdbc:mysql://hadoop-master:3306/stormproject");
        jdbconfig.setUsername("stormproject");
        jdbconfig.setPassword("storm");
        jdbcUtil = new JDBCUtil(jdbconfig.getDriver(),
                jdbconfig.getUrl(),
                jdbconfig.getUsername(),
                jdbconfig.getPassword());
        jdbcUtil.init();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("flag"));
    }
}
