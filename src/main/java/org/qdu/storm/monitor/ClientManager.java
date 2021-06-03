package org.qdu.storm.monitor;

import org.apache.storm.generated.Nimbus;
import org.apache.storm.thrift.protocol.TBinaryProtocol;
import org.apache.storm.thrift.protocol.TProtocol;
import org.apache.storm.thrift.transport.TFramedTransport;
import org.apache.storm.thrift.transport.TSocket;
import org.apache.storm.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;


/**
 * Thrift Client管理类
 * @author jb-xingchencheng
 *
 */
public class ClientManager {
    public static ClientInfo getClient(String nimbusHost, int nimbusPort) throws TTransportException, org.apache.storm.thrift.transport.TTransportException {
        ClientInfo client = new ClientInfo();
        TSocket tsocket = new TSocket(nimbusHost, nimbusPort);
        TTransport tTransport = new TFramedTransport(tsocket);
        TProtocol tBinaryProtocol = new TBinaryProtocol(tTransport);
        Nimbus.Client c = new Nimbus.Client(tBinaryProtocol);
        tTransport.open();
        client.setTsocket(tsocket);
        client.settTransport(tTransport);
        client.settBinaryProtocol(tBinaryProtocol);
        client.setClient(c);

        return client;
    }

    public static void closeClient(ClientInfo client) {
        if (null == client) {
            return;
        }

        if (null != client.gettTransport()) {
            client.gettTransport().close();
        }

        if (null != client.getTsocket()) {
            client.getTsocket().close();
        }
    }
}