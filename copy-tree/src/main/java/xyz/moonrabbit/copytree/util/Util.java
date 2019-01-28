package xyz.moonrabbit.copytree.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import xyz.moonrabbit.copytree.Launcher;

import java.io.File;
import java.io.PrintStream;
import java.net.URISyntaxException;

public class Util {

    public static String getJarFileName() {
        try {
            String fileName = new File(Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getName();
            return StringUtils.substringBefore(FilenameUtils.removeExtension(fileName), "-");
        } catch (URISyntaxException e) {
            return "copy-tree";
        }
    }

    public static void out() {
        out("");
    }

    public static void out(String format, Object... args) {
        println(System.out, format, args);
    }

    public static void err(String format, Object... args) {
        println(System.err, format, args);
    }

    private static void println(PrintStream printStream, String format, Object... args) {
        String text = String.format(format, args);
        printStream.println(text);
    }
}