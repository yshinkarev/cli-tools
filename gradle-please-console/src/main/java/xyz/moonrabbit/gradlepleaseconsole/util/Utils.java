package xyz.moonrabbit.gradlepleaseconsole.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class Utils {

    public  static String getHtmlPage(String httpUrl) throws IOException {
        URL url = new URL(httpUrl);
        URLConnection connection = url.openConnection();
        StringBuilder strings = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                strings.append(line).append(newLine);
            }
        }

        return strings.toString();
    }
}
