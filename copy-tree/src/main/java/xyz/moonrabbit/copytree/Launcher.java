package xyz.moonrabbit.copytree;

import xyz.moonrabbit.Util;
import xyz.moonrabbit.copytree.util.CliHelper;

public class Launcher {

    public static void main(String[] args) {
        CliHelper cliHelper = new CliHelper(args);
        if (!cliHelper.parse()) {
            return;
        }

        long start = System.currentTimeMillis();

        try {

        } finally {
            Util.out();
            Util.out("Total processed time: %ds", (System.currentTimeMillis() - start) / 1_000L);
        }
    }
}