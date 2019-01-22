package xyz.moonrabbit.photorenamer;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorLogger {

    private StringBuilder builder = new StringBuilder();

    public ErrorLogger append(String text) {
        builder.append(text);
        return this;
    }

    public ErrorLogger append(Exception e) {
        PrintWriter pw = null;
        try {
            StringWriter sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return append(sw.toString());
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    public ErrorLogger tab() {
        return append("   ");
    }

    public ErrorLogger line() {
        return append(System.getProperty("line.separator"));
    }

    public void log() {
        if (builder.length() > 0) {
            System.err.println(builder);
        }
    }

    public void clear() {
        builder = new StringBuilder();
    }
}