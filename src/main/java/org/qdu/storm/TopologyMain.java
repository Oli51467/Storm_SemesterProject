package org.qdu.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.qdu.storm.bolts.Split;
import org.qdu.storm.bolts.TestOfPrint;
import org.qdu.storm.spouts.LinesReader;

public class TopologyMain {
    public static void main(String[] args) throws Exception {
        String toponame;
        toponame=args[0];
        if(args==null||args.length==0){
            System.err.println("setup a name first");
            return;
        }

        TopologyBuilder builder = new TopologyBuilder();
        //设置topo
        builder.setSpout("sourcedata",new LinesReader());

        builder.setBolt("split",new Split()).shuffleGrouping("sourcedata");

        //打印测试
        //builder.setBolt("print",new TestOfPrint()).shuffleGrouping("split");

        //Config
        Config config = new Config();
        config.put("logfile","D:/Storm/app.log");
        config.setDebug(true);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology(toponame,config,builder.createTopology());
    }
}
