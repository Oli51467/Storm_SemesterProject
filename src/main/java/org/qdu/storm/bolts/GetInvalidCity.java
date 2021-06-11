package org.qdu.storm.bolts;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.trident.state.StateType;
import org.apache.storm.tuple.Tuple;
import org.qdu.storm.jdbcUtils.JDBCStateConfig;
import org.qdu.storm.jdbcUtils.JDBCUtil;

import java.util.Map;
/*
将无法映射成坐标的城市保存到数据库中
 */
public class GetInvalidCity extends BaseRichBolt {

    OutputCollector collector;
    JDBCStateConfig jdbconfig;
    JDBCUtil jdbcUtil;
    final private String tablename="InvalidCity";

    @Override
    public void prepare(Map<String, Object> map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
        setting(map);
    }

    @Override
    public void execute(Tuple tuple) {
        String unbindc = tuple.getStringByField("unbind");
        String sql = "insert into " + tablename +" (city) values('"+unbindc+"');";
        jdbcUtil.insert(sql);
        collector.ack(tuple);
    }

    void setting(Map<String,Object> conf){
        jdbconfig = new JDBCStateConfig();
        //容错性
        jdbconfig.setType(StateType.TRANSACTIONAL);
        //设置mysql相关配置信息
        jdbconfig.setDriver(conf.get("driver").toString());
        jdbconfig.setTable(conf.get("table").toString());
        jdbconfig.setUrl(conf.get("url").toString());
        jdbconfig.setUsername(conf.get("username").toString());
        jdbconfig.setPassword(conf.get("password").toString());
        //将设置好的配置信息传入到JDBCUtil中
        jdbcUtil = new JDBCUtil(jdbconfig.getDriver(),
                jdbconfig.getUrl(),
                jdbconfig.getUsername(),
                jdbconfig.getPassword());
        jdbcUtil.init();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
