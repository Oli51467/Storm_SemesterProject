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
    JDBCStateConfig jdbcConfig;
    JDBCUtil jdbcUtil;

    @Override
    public void prepare(Map<String, Object> map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
        setting(map);
    }

    @Override
    public void execute(Tuple tuple) {
        String unbind = tuple.getStringByField("unbind");
        String tableName = "InvalidCity";
        String sql = "insert into " + tableName + " (city) values('" + unbind + "');";
        jdbcUtil.insert(sql);
        collector.ack(tuple);
    }

    void setting(Map<String, Object> conf) {
        jdbcConfig = new JDBCStateConfig();
        //容错性
        jdbcConfig.setType(StateType.TRANSACTIONAL);
        //设置mysql相关配置信息
        jdbcConfig.setDriver(conf.get("driver").toString());
        jdbcConfig.setTable(conf.get("table").toString());
        jdbcConfig.setUrl(conf.get("url").toString());
        jdbcConfig.setUsername(conf.get("username").toString());
        jdbcConfig.setPassword(conf.get("password").toString());
        //将设置好的配置信息传入到JDBCUtil中
        jdbcUtil = new JDBCUtil(jdbcConfig.getDriver(),
                jdbcConfig.getUrl(),
                jdbcConfig.getUsername(),
                jdbcConfig.getPassword());
        jdbcUtil.init();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
