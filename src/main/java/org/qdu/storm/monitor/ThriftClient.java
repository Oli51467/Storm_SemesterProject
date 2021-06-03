package org.qdu.storm.monitor;

import org.apache.storm.generated.Nimbus;
import org.apache.storm.thrift.protocol.TBinaryProtocol;
import org.apache.storm.thrift.protocol.TProtocol;
import org.apache.storm.thrift.transport.TFramedTransport;
import org.apache.storm.thrift.transport.TTransport;
import org.apache.storm.thrift.transport.TSocket;


public class ThriftClient {
    // IP of the Storm UI node
    private static final String STORM_UI_NODE = "niit-master";

    public Nimbus.Client getClient() {
        // Set the IP and port of thrift server.
        // By default, the thrift server start on port 6627
        TSocket socket = new TSocket(STORM_UI_NODE, 6627);
        TTransport tFramedTransport = new TFramedTransport(socket);
        TProtocol tBinaryProtocol = new TBinaryProtocol(tFramedTransport);
        Nimbus.Client client = new Nimbus.Client(tBinaryProtocol);

        try {
            // Open the connection with thrift client
            tFramedTransport.open();
        } catch (Exception exception) {
            throw new RuntimeException("Error occurred while making connection with nimbus thrift server");
        }
        // return the Nimbus Thrift client.
        return client;
    }
}

