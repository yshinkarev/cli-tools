package xyz.moonrabbit.gradlepleaseconsole.parser;

public class JcenterParser extends AbstractParser {

    @Override
    protected String getUrl(String search) {
        // https://bintray.com/api/v1/search/packages/maven?q=*commons-cli*
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    protected void processInternal(String content) {

    }
}