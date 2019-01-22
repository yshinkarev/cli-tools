package xyz.moonrabbit.gradlepleaseconsole.parser;

import xyz.moonrabbit.gradlepleaseconsole.util.Utils;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractParser implements Parser {

    @Override
    public final void process(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("gradle-please-console: Missing SEARCH PATTERN");
            System.exit(1);
            return;
        }

        String search = Stream.of(args).collect(Collectors.joining()).replace(" ", "%20");
        String content = Utils.getHtmlPage(getUrl(search));
        processInternal(content);
    }

    protected abstract String getUrl(String search);

    protected abstract void processInternal(String content);
}