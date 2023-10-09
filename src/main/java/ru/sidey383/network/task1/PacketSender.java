package ru.sidey383.network.task1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PacketSender extends Thread {

    private final UUID uuid;

    private final InetAddress address;

    private final int port;


    public PacketSender(UUID uuid, InetAddress address, int port) {
        this.uuid = uuid;
        this.address = address;
        this.port = port;
        if (port < 0 || port > 65535)
            throw new IllegalArgumentException("Wrong port " + port);
        if (!address.isMulticastAddress())
            throw new IllegalArgumentException("Not multicast address");
    }

    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                DatagramSocket socket = new DatagramSocket();
                byte[] buf = uuid.toString().getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
                socket.close();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            System.out.println("Stop sender task");
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

}
