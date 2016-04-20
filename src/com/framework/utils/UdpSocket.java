package com.framework.utils;

import java.io.IOException;
import java.net.*;

public class UdpSocket {
    public UdpSocket(String host, int port) {
        host_ = host;
        port_ = port;
    }

    public void send(byte[] buffer) throws IOException {
        DatagramSocket ds  = new DatagramSocket();
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(host_), port_);
        ds.send(dp);
        ds.close();
    }

    private String host_;
    private int port_;
}
