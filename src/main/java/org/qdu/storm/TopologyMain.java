package org.qdu.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.trident.state.StateType;
import org.qdu.storm.bolts.*;
import org.qdu.storm.jdbcUtils.JDBCStateConfig;
import org.qdu.storm.spouts.LinesReader;

public class TopologyMain {
    public static void main(String[] args) throws Exception {
        String toponame;
        toponame=args[0];
        if(args==null||args.length==0) {
            System.err.println("setup a name first");
            return;
        }

        TopologyBuilder builder = new TopologyBuilder();

        //设置topo
        //数据源
        builder.setSpout("sourcedata",new LinesReader());
        //切分
        builder.setBolt("split",new Split()).shuffleGrouping("sourcedata");

        //转化ip到long
        builder.setBolt("iptolong",new ipToLong()).shuffleGrouping("split");
        //ip转城市
        builder.setBolt("addrtocity",new AddrToCity()).shuffleGrouping("iptolong");

        //builder.setBolt("trim",new TrimSuffix()).shuffleGrouping("addrtocity");

        //城市转坐标
        builder.setBolt("getxy",new CityToCoordinate()).shuffleGrouping("addrtocity");

        //坐标估值
        builder.setBolt("eva",new eval()).shuffleGrouping("getxy",CityToCoordinate.Stream_ID_1);

        //没有映射的城市
        builder.setBolt("unbindcity",new GetInvalidCity()).shuffleGrouping("getxy",CityToCoordinate.Stream_ID_2);

        //坐标持久化
        builder.setBolt("database",new jdbcConnector()).shuffleGrouping("eva");
        //打印测试
        builder.setBolt("print",new TestOfPrint()).shuffleGrouping("database");

        //设置发射数据源和映射文件
        Config config = new Config();
        config.put("logfile","D:/Storm/app.log");
        config.put("refile","D:/Storm/ip_area_isp.txt");
        config.put("driver","com.mysql.jdbc.Driver");
        config.put("table","Location");
        config.put("url","jdbc:mysql://hadoop-master:3306/stormproject");
        config.put("username","stormproject");
        config.put("password","storm");
        config.setDebug(true);
        //config.setNumWorkers(2);

        //本地模式调试
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology(toponame,config,builder.createTopology());

        //集群模式
        //StormSubmitter.submitTopology(toponame,config, builder.createTopology());
    }
}
