package org.qdu.storm.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class DownLoad {
    public DownLoad() throws IOException, URISyntaxException {

        FileSystem fs = FileSystem.get(new URI("hdfs://192.168.40.132:9000"),new Configuration());
        InputStream in = fs.open(new Path("/data/ip_area_isp.txt"));

        OutputStream out = new FileOutputStream("D:/Storm/ip_area_isp.txt");
        IOUtils.copyBytes(in, out,4096,true);
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        DownLoad demo = new DownLoad();
    }
}
