package org.qdu.storm.monitor;

import org.apache.storm.generated.Nimbus;

public class NimbusConfiguration {
    public void printNimbusStats() {
        try {
            ThriftClient thriftClient = new ThriftClient();
            Nimbus.Client client = thriftClient.getClient();
            String nimbusConiguration = client.getNimbusConf();
            System.out.println("*************************************");
            System.out.println("Nimbus Configuration : " + nimbusConiguration);
            System.out.println("*************************************");

        } catch (Exception exception) {
            throw new RuntimeException("Error occurred while fetching the Nimbus statistics : ");
        }
    }

    public static void main(String[] args) {
        new NimbusConfiguration().printNimbusStats();

    }
}
