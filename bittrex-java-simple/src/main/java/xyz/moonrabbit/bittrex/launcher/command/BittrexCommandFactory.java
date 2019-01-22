package xyz.moonrabbit.bittrex.launcher.command;

import org.apache.commons.lang3.StringUtils;

public class BittrexCommandFactory {

    public static BittrexCommand get(String command) {
        if (StringUtils.isEmpty(command)) {
            return null;
        }

        if (command.equals("get-balances")) {
            return new BittrexGetBalancesCommand();
        }

        return null;
    }
}