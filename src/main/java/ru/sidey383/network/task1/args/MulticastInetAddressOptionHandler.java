package ru.sidey383.network.task1.args;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.InetAddressOptionHandler;
import org.kohsuke.args4j.spi.Messages;
import org.kohsuke.args4j.spi.Setter;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MulticastInetAddressOptionHandler extends InetAddressOptionHandler {

    public MulticastInetAddressOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super InetAddress> setter) {
        super(parser, option, setter);
    }

    @Override
    protected InetAddress parse(String argument) throws CmdLineException {
        try {
            InetAddress address = InetAddress.getByName(argument);
            if (address.isMulticastAddress()) {
                return address;
            } else {
                throw new CmdLineException(owner,
                        Messages.ILLEGAL_IP_ADDRESS, argument);
            }
        } catch (UnknownHostException e) {
            throw new CmdLineException(owner,
                    Messages.ILLEGAL_IP_ADDRESS, argument);
        }
    }
}
