package xyz.moonrabbit.gradlepleaseconsole.parser;

import java.io.IOException;

public interface Parser {

    void process(String[] args) throws IOException;
}