package xyz.moonrabbit.bittrex.launcher.command;

import xyz.moonrabbit.bittrex.Bittrex;

public class BittrexGetBalancesCommand implements BittrexCommand {

    @Override
    public String execute(Bittrex bittrex, String arguments) throws Exception {
        return bittrex.getBalances();
    }
}