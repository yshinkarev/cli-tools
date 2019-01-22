package xyz.moonrabbit.gradlepleaseconsole.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GradlePleaseWebParser extends AbstractParser {

    @Override
    protected String getUrl(String search) {
        return "http://gradleplease.appspot.com/#" + search;
    }

    @Override
    protected void processInternal(String content) {
        // <pre id="suggestion">
        // dependencies {
        //     compile '<span>..........</span>'
        // }
        Pattern pattern = Pattern.compile("<pre id=\"suggestion\">(.*)<div id=\"feedback\">", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        if (!matcher.find()) {
            System.err.println("Something wrong. Can not find by regex");
            System.exit(1);
        }

        System.out.println(matcher.group(1)
                .trim()
                .replaceAll("<[^>]*>", "")); // Remove span tag.
    }
}