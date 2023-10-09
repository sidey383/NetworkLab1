package ru.sidey383.network.task1;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PacketReciver extends Thread {
    private final int port;

    private final InetAddress address;

    private Set<AppInfo> availableValues = new HashSet<>();

    public PacketReciver(InetAddress address, int port) {
        this.address = address;
        this.port = port;
        if (port < 0 || port > 65535)
            throw new IllegalArgumentException("Wrong port " + port);
        if (!address.isMulticastAddress()) {
            throw new IllegalArgumentException("Not multicast address");
        }
    }

    public Set<AppInfo> collectActiveApps() {
        synchronized (this) {
            Set<AppInfo> set = this.availableValues;
            this.availableValues = new HashSet<>();
            return set;
        }
    }

    public void run() {
        try (MulticastSocket socket = new MulticastSocket(port)) {
            byte[] buffer = new byte[4096];
            socket.joinGroup(address);
            socket.setSoTimeout(200);
            while (!Thread.currentThread().isInterrupted()) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(packet);
                } catch (SocketTimeoutException e) {
                    continue;
                }
                String data = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
                UUID receive = null;
                try {
                    receive = UUID.fromString(data);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace(System.err);
                }
                synchronized (this) {
                    InetAddress source = packet.getAddress();
                    String host = source.getHostName();
                    availableValues.add(new AppInfo(receive, host));
                }
            }
            System.out.println("Stop receiver task");
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

}
