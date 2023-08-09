package org.qdu.storm.monitor;

import org.apache.storm.generated.Nimbus;
import org.apache.storm.thrift.protocol.TProtocol;
import org.apache.storm.thrift.transport.TTransport;
import org.apache.storm.thrift.transport.TSocket;

/**
 * 代表一个Thrift Client的信息
 *
 * @author jb-xingchencheng
 */
public class ClientInfo {
    private TSocket tsocket;
    private TTransport tTransport;
    private TProtocol tBinaryProtocol;
    private Nimbus.Client client;

    public TSocket getTsocket() {
        return tsocket;
    }

    public void setTsocket(TSocket tsocket) {
        this.tsocket = tsocket;
    }

    public TTransport gettTransport() {
        return tTransport;
    }

    public void settTransport(TTransport tTransport) {
        this.tTransport = tTransport;
    }

    public TProtocol gettBinaryProtocol() {
        return tBinaryProtocol;
    }

    public void settBinaryProtocol(TProtocol tBinaryProtocol) {
        this.tBinaryProtocol = tBinaryProtocol;
    }

    public Nimbus.Client getClient() {
        return client;
    }

    public void setClient(Nimbus.Client client) {
        this.client = client;
    }
}