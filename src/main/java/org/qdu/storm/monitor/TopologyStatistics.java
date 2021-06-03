package org.qdu.storm.monitor;

import org.apache.storm.generated.ClusterSummary;
import org.apache.storm.generated.Nimbus;
import org.apache.storm.generated.TopologySummary;

import java.util.Iterator;

public class TopologyStatistics {
    public void printTopologyStatistics() {
        try {
            ThriftClient thriftClient = new ThriftClient();
            // Get the thrift client
            Nimbus.Client client = thriftClient.getClient();
            // Get the cluster info
            ClusterSummary clusterSummary = client.getClusterInfo();
            // Get the interator over TopologySummary class
            Iterator<TopologySummary> topologiesIterator =
                    clusterSummary.get_topologies_iterator();
            while (topologiesIterator.hasNext()) {
                TopologySummary topologySummary = topologiesIterator.next();
                System.out.println("*************************************");
                System.out.println("ID of topology: " + topologySummary.get_id());
                System.out.println("Name of topology: " + topologySummary.get_name());
                System.out.println("Number of Executors: " + topologySummary.get_num_executors());
                System.out.println("Number of Tasks: " + topologySummary.get_num_tasks());
                System.out.println("Number of Workers: " + topologySummary.get_num_workers());
                System.out.println("Status of topology: " + topologySummary.get_status());
                System.out.println("Topology uptime in seconds: " + topologySummary.get_uptime_secs());
                System.out.println("*************************************");
            }
        } catch (Exception exception) {
            throw new RuntimeException("Error occurred while fetching the topologies information");
        }
    }

    public static void main(String[] args) {

        new TopologyStatistics().printTopologyStatistics();
    }
}
