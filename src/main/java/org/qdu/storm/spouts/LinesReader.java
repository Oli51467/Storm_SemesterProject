package org.qdu.storm.spouts;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

public class LinesReader extends BaseRichSpout {

    private SpoutOutputCollector collector;
    private FileReader fileReader;
    private boolean completed = false;

    @Override
    public void open(Map<String, Object> conf, TopologyContext context, SpoutOutputCollector spoutOutputCollector) {
        try {
            this.fileReader = new FileReader(conf.get("logfile").toString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error reading file ["+conf.get("logfile")+"]");
        }
        //初始化发射器
        this.collector=spoutOutputCollector;
    }

    @Override
    public void nextTuple() {
        /*if(completed){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }*/
        String line;
        //Open the reader
        BufferedReader reader = new BufferedReader(fileReader);
        try{
            //读取每一行
            while((line = reader.readLine()) != null){
                //this.collector.emit(new Values(line),line);
                this.collector.emit(new Values(line));
            }
        }catch(Exception e){
            throw new RuntimeException("Error reading tuple",e);
        }finally{
            completed = true;
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("line"));
    }

    @Override
    public void ack(Object msgId) {
        System.out.println("OK:" + msgId);
    }
    @Override
    public void fail(Object msgId) {
        System.out.println("FAIL:" + msgId);
    }

    @Override
    public void close() {
    }
}
