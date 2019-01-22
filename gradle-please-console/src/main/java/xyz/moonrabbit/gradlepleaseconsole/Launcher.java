package xyz.moonrabbit.gradlepleaseconsole;

import xyz.moonrabbit.gradlepleaseconsole.parser.GradlePleaseSearchParser;

import java.io.IOException;

public class Launcher {

    public static void main(String[] args) throws IOException {
//        new GradlePleaseWebParser().process(args);
        new GradlePleaseSearchParser().process(args);
    }
}