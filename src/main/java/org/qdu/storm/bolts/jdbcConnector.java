package org.qdu.storm.bolts;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.trident.state.StateType;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.qdu.storm.jdbcUtils.JDBCStateConfig;
import org.qdu.storm.jdbcUtils.JDBCUtil;
import java.util.Map;

/*
    连接数据库bolts，将接收到的经纬度坐标
    持久化到数据库中
 */
public class jdbcConnector extends BaseRichBolt {

    OutputCollector collector;
    double longti;
    double lati;
    JDBCStateConfig jdbconfig;
    JDBCUtil jdbcUtil;
    String tablename;

    //定义插入到数据库中的相关字段的值，name默认为地区
    int value =10;
    String name1 = "地区";
    String city1;


    @Override
    public void prepare(Map<String, Object> conf, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;

        //初始化数据库连接配置信息
        setting(conf);
    }

    @Override
    public void execute(Tuple tuple) {
        //取字段
        longti = tuple.getDoubleByField("lng");
        lati = tuple.getDoubleByField("lat");
        city1 = tuple.getStringByField("CITY");
        value = tuple.getIntegerByField("value");

        //获取要持久化的表名
        tablename = jdbconfig.getTable();
        //prepare一条sql语句
        String sql = "insert into " + tablename +" (name,city,value,lng,lat) values('"+name1+"','"+city1+"',"+value+","+longti+","+lati+");";
        //插入到数据库中
        jdbcUtil.insert(sql);
        collector.emit(new Values("success"));
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
        outputFieldsDeclarer.declare(new Fields("flag"));
    }
}
