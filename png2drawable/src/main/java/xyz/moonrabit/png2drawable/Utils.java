package xyz.moonrabit.png2drawable;

public class Utils {

    public static String stripExtenstion(String fileName) {
        if (fileName != null && fileName.indexOf(".") > 0) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        } else {
            return fileName;
        }
    }
}