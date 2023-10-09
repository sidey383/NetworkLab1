package ru.sidey383.network.task1.args;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.IntOptionHandler;
import org.kohsuke.args4j.spi.Setter;

public class PortOptionHandler extends IntOptionHandler {

    public PortOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super Integer> setter) {
        super(parser, option, setter);
    }

    @Override
    protected Integer parse(String argument) throws NumberFormatException {
        int port = Integer.parseInt(argument);
        if (port < 0 || port > 65535)
            throw new NumberFormatException("Number out of range " + port);
        return port;
    }
}
