package ru.sidey383.network.task1.args;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.UuidOptionHandler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConsoleArgs {

    @Option(name = "--uuid", aliases = {"-u"}, handler = UuidOptionHandler.class, usage = "app uuid address", help = true)
    private UUID uuid = UUID.randomUUID();
    @Option(name = "--port", aliases = {"-p"}, handler = PortOptionHandler.class, usage = "used port", help = true)
    private int port = 233;
    @Option(name = "--address", aliases = {"-a"}, handler = MulticastInetAddressOptionHandler.class, usage = "used multicast address", help = true)
    private InetAddress inetAddress;

    @Argument
    private List<String> arguments = new ArrayList<>();

    {
        try {
            inetAddress = InetAddress.getByName("224.0.0.38");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public boolean hasHelp() {
        return arguments.contains("--help") || arguments.contains("-h");
    }

}
