package xyz.moonrabbit.gradlepleaseconsole.parser;

import org.json.JSONArray;
import org.json.JSONObject;

public class GradlePleaseSearchParser extends AbstractParser {

    private final static String PREFIX = "searchCallback(";
    private final static String SUFFIX = ")";

    @Override
    protected String getUrl(String search) {
        return "http://gradleplease.appspot.com/search?q=" + search;
    }

    @Override
    protected void processInternal(String content) {
        content = content.trim();
        if (!content.startsWith(PREFIX) || !content.endsWith(SUFFIX)) {
            System.err.println("Something wrong. Wrong response");
            System.exit(1);
        }

        String jsonContent = content.substring("searchCallback(".length(), content.length() - SUFFIX.length());

        JSONObject json = new JSONObject(jsonContent);

        if (json.optBoolean("error")) {
            System.err.println("Nothing found :-(");
            System.exit(1);
        }

        JSONArray array = json.getJSONObject("response").getJSONArray("docs");
        json = array.getJSONObject(0);
        System.out.println(json.getString("id") + ":" + json.getString("latestVersion"));
    }
}