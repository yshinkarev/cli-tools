package xyz.moonrabbit.bittrex.launcher;

import org.json.JSONObject;
import xyz.moonrabbit.bittrex.Bittrex;
import xyz.moonrabbit.bittrex.launcher.command.BittrexCommand;

import java.util.Map;

public class Launcher {

    // --key-file=./src/main/resources/keys.properties --get-balances
    public static void main(String[] args) throws Exception {
        CommandArgumentsResolver resolver = new CommandArgumentsResolver(args);
        Bittrex bittrex = resolver.createBittrex();

        Map<BittrexCommand, String> commands = resolver.parseCommands();

        if (commands.isEmpty()) {
            System.out.println("No commands to execute");
            return;
        }

        for (Map.Entry<BittrexCommand, String> entry : commands.entrySet()) {
            BittrexCommand command = entry.getKey();
            String arguments = entry.getValue();
            System.out.printf("\nCommand: [%s]. Arguments: [%s] \n", command, arguments);

            String result = command.execute(bittrex, arguments);
            logPrettyJson(null, result);
        }

//        logPrettyJson("Balances", bittrex.getBalances());
//        logPrettyJson("BuyLimit", bittrex.buyLimit("BTC-ETH", "0.0000001", "0.0000001"));
//        logPrettyJson("SellLimit", bittrex.sellLimit("BTC-ETH", "0.0000001", "0.0000001"));
//        logPrettyJson("BuyMarket", bittrex.buyMarket("BTC-ETH", "0.0000001"));
//        logPrettyJson("SellMarket", bittrex.sellMarket("BTC-ETH", "0.0000001"));
//        logPrettyJson("Currencies", bittrex.getCurrencies());
//        logPrettyJson("DepositHistory", bittrex.getDepositHistory());
//        logPrettyJson("OpenOrders", bittrex.getOpenOrders());
    }

    public static void logPrettyJson(String message, Object object) {
        if (message != null) {
            System.out.println(message + ":");
        }
        System.out.println(new JSONObject(object.toString()).toString(4));
    }
}