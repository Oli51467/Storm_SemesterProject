package org.qdu.storm.monitor;

import org.apache.storm.generated.ClusterSummary;
import org.apache.storm.generated.Nimbus;
import org.apache.storm.generated.SupervisorSummary;

import java.util.Iterator;

public class SupervisorStatistics {
    public void printSupervisorStatistics() {
        try {
            ThriftClient thriftClient = new ThriftClient();
            Nimbus.Client client = thriftClient.getClient();
            // Get the cluster information.
            ClusterSummary clusterSummary = client.getClusterInfo();
            // Get the SupervisorSummary iterator
            Iterator<SupervisorSummary> supervisorsIterator = clusterSummary.get_supervisors_iterator();
            while (supervisorsIterator.hasNext()) {
                // Print the information of supervisor node
                SupervisorSummary supervisorSummary = (SupervisorSummary) supervisorsIterator.next();
                System.out.println("*************************************");
                System.out.println("Supervisor Host IP : " + supervisorSummary.get_host());
                System.out.println("Number of used workers : " + supervisorSummary.get_num_used_workers());
                System.out.println("Number of workers : " + supervisorSummary.get_num_workers());
                System.out.println("Supervisor ID : " + supervisorSummary.get_supervisor_id());
                System.out.println("Supervisor uptime in seconds : " + supervisorSummary.get_uptime_secs());
                System.out.println("*************************************");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while getting cluster info: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new SupervisorStatistics().printSupervisorStatistics();
    }
}

