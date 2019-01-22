package xyz.moonrabbit.bittrex.launcher.command;

import xyz.moonrabbit.bittrex.Bittrex;

public interface BittrexCommand {

    String execute(Bittrex bittrex, String arguments) throws Exception;
}
