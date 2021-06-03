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
        builder.setSpout("sourcedata",new LinesReader()).setNumTasks(2);
        //切分
        builder.setBolt("split",new Split(),2).shuffleGrouping("sourcedata")
                                                .setNumTasks(4);
        //转化ip到long
        builder.setBolt("iptolong",new ipToLong(),2).shuffleGrouping("split")
                                                                    .setNumTasks(4);
        //ip转城市
        builder.setBolt("addrtocity",new AddrToCity(),2).shuffleGrouping("iptolong")
        .setNumTasks(2);

        //builder.setBolt("trim",new TrimSuffix()).shuffleGrouping("addrtocity");

        //城市转坐标
        builder.setBolt("getxy",new CityToCoordinate(),2).shuffleGrouping("addrtocity")
                                                                            .setNumTasks(2);

        //坐标估值
        builder.setBolt("eva",new eval()).shuffleGrouping("getxy");

        //坐标持久化
        builder.setBolt("database",new jdbcConnector(),4).shuffleGrouping("eva")
        .setNumTasks(4);
        //打印测试
        //builder.setBolt("print",new TestOfPrint()).shuffleGrouping("database");

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
        config.setNumWorkers(2);

        //本地模式调试
        //LocalCluster cluster = new LocalCluster();
        //cluster.submitTopology(toponame,config,builder.createTopology());

        //集群模式
        StormSubmitter.submitTopology(toponame,config, builder.createTopology());
    }
}
