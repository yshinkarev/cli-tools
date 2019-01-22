package xyz.moonrabbit.gradlepleaseconsole.parser;

public class MavenCentralParser extends AbstractParser {

    @Override
    protected String getUrl(String search) {
        // https://search.maven.org/solrsearch/select?wt=json&q=commons-cli
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    protected void processInternal(String content) {

    }
}