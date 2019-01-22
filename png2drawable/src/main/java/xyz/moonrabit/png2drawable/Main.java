package xyz.moonrabit.png2drawable;

/**
 * Create for input image file output images (png) in sub directories drawable-*
 * Using: java -jar png2drawable-1.0.jar test.png
 */
public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Missing argument FILENAME");
            System.exit(1);
            return;
        }

        try (ImageResizer resizer = new ImageResizer(args[0])) {
            if (resizer.isValid()) {
                if (resizer.process()) {
                    return;
                }
            }

            System.exit(resizer.getErrorCode());
        }
    }
}