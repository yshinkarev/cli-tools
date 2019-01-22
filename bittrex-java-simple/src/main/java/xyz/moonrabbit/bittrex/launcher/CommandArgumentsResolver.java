package xyz.moonrabbit.bittrex.launcher;

import org.apache.commons.lang3.math.NumberUtils;
import xyz.moonrabbit.bittrex.Bittrex;
import xyz.moonrabbit.bittrex.BittrexBuilder;
import xyz.moonrabbit.bittrex.launcher.command.BittrexCommand;
import xyz.moonrabbit.bittrex.launcher.command.BittrexCommandFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandArgumentsResolver {

    private final Map<String, String> arguments;

    public CommandArgumentsResolver(String[] args) {
        arguments = Arrays.stream(args)
                .filter(arg -> arg.startsWith("--"))
                .map(arg -> arg.substring(2).toLowerCase())
                .filter(arg -> !arg.isEmpty())
                .map(arg -> arg.split("="))
                .collect(Collectors.toMap(arg -> arg[0], arg -> arg.length > 1 ? arg[1] : ""));
    }

    public Bittrex createBittrex() throws IOException {
        BittrexBuilder builder = new BittrexBuilder();

        builder.setApikey(argumentToString("apikey"));
        builder.setSecret(argumentToString("secret"));
        builder.setRetryAttempts(argumentToInt("retry-attempts"));
        builder.setRetryDelaySeconds(argumentToInt("retry-delay-seconds"));

        String filename = argumentToString("key-file");
        if (filename != null) {
            builder.setKeyFileStream(new FileInputStream(filename));
        }

        return builder.build();
    }

    public Map<BittrexCommand, String> parseCommands() {
        Map<BittrexCommand, String> ret = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : arguments.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            BittrexCommand command = BittrexCommandFactory.get(key);
            if (command != null) {
                ret.put(command, value);
            }
        }

        return ret;
    }

    private String argumentToString(String name) {
        return arguments.remove(name);
    }

    private int argumentToInt(String name) {
        return NumberUtils.toInt(arguments.get(name));
    }
}