package org.qdu.storm.monitor;

import org.apache.storm.generated.Nimbus;

public class killTopology {
    public void kill(String topologyName) {
        try {
            ThriftClient thriftClient = new ThriftClient();
            // Get the nimbus thrift client
            Nimbus.Client client = thriftClient.getClient();
            // kill the given topology
            client.killTopology(topologyName);
        } catch (Exception exception) {
            throw new RuntimeException("Error occurred while killing the topology : " + exception);
        }
    }

    public static void main(String[] args) {
        new killTopology().kill("wordcount");
    }
}
