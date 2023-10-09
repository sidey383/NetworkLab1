package ru.sidey383.network.task1;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import ru.sidey383.network.task1.args.ConsoleArgs;

import java.io.*;
import java.util.Collection;

public class Main {

    private final PacketReciver packetReciver;

    private final PacketSender packetSender;

    private Thread reader;

    private Thread writer;

    public Main(ConsoleArgs args) {
        this.packetReciver = new PacketReciver(args.getInetAddress(), args.getPort());
        this.packetSender = new PacketSender(args.getUuid(), args.getInetAddress(), args.getPort());
        System.out.println("My uuid: " + args.getUuid());
        System.out.println("Address: " + args.getInetAddress());
        System.out.println("Port: " + args.getPort());
    }

    public static void main(String[] args) {
        ConsoleArgs consoleArgs = new ConsoleArgs();
        CmdLineParser parser = new CmdLineParser(consoleArgs);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getLocalizedMessage());
            parser.printUsage(System.out);
            return;
        }
        if (consoleArgs.hasHelp()) {
            parser.printUsage(System.out);
            return;
        }
        Main m = new Main(consoleArgs);
        m.run();
    }

    public void run() {
        this.packetSender.start();
        this.packetReciver.start();
        if (writer == null || !writer.isAlive()) {
            writer = new Thread(this::consoleWriter);
            writer.start();
        }
        if (reader == null || !reader.isAlive()) {
            reader = new Thread(this::consoleReader);
            reader.start();
        }
    }

    public void stop() {
        this.packetSender.interrupt();
        this.packetReciver.interrupt();
        if (writer != null && writer.isAlive())
            writer.interrupt();
        if (reader != null && reader.isAlive()) {
            reader.interrupt();
        }
    }

    private String inputString = "";

    public synchronized String getInputString() {
        return inputString;
    }

    public synchronized String removeInputString() {
        String str = inputString;
        inputString = "";
        return str;
    }

    public synchronized void appendInputString(char character) {
        this.inputString += character;
    }

    public void consoleReader() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                int val = System.in.read();
                if (val == -1)
                    break;
                if (val != '\n' && val != '\r') {
                    appendInputString((char) val);
                    continue;
                }
                String str = removeInputString();
                switch (str) {
                    case "" -> {}
                    case "exit", "close", "stop" -> {
                        stop();
                        System.out.println("Stop reader task");
                        return;
                    }
                    default -> System.out.println("Unknown command: \"" + str + "\"");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void consoleWriter() {
        try (Writer writer = new OutputStreamWriter(System.out)) {
            while (true) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Stop writer task");
                    return;
                }
                Collection<AppInfo> apps = packetReciver.collectActiveApps();
                writer.append("\b".repeat(getInputString().length()));
                writer.append("Active programs:\n");
                for (AppInfo app : apps) {
                    writer.append("\t").append(app.toString()).append("\n");
                }
                writer.append(getInputString());
                writer.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
