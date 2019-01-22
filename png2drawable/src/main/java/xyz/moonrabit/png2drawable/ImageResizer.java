package xyz.moonrabit.png2drawable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageResizer implements AutoCloseable {

    private final String fileName;
    private String targetFileName;
    private int imageSize;
    private BufferedImage image;
    private int errorCode;

    private final static String[] DENSITIES = new String[]{"mdpi", "hdpi", "xhdpi", "xxhdpi", "xxxhdpi"};
    private final static int[] SIZE = new int[]{24, 36, 48, 72, 96};

    public ImageResizer(String fileName) {
        this.fileName = fileName;
    }

    public boolean isValid() {
        try {
            File file = new File(fileName);
            targetFileName = Utils.stripExtenstion(file.getName()) + ".png";
            image = ImageIO.read(file);
        } catch (IOException e) {
            System.err.println("Error read file");
            errorCode = 2;
            return false;
        }

        if (image.getWidth() != image.getHeight()) {
            System.err.println("Image is not square");
            errorCode = 3;
            return false;
        }

        imageSize = image.getWidth();

        if (imageSize < 24) {
            System.err.println("Minimal image size is 24");
            errorCode = 4;
            return false;
        }

        return true;
    }

    public boolean process() {
        if (DENSITIES.length != SIZE.length) {
            throw new IllegalStateException();
        }

        boolean ret = true;

        for (int i = 0; i < DENSITIES.length; i++) {
            String densityName = DENSITIES[i];
            int targetSize = SIZE[i];
            if (!processDensity(densityName, targetSize)) {
                ret = false;
            }
        }

        return ret;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public void close() {
    }

    private boolean processDensity(String densityName, int targetSize) {
        if (targetSize > imageSize) {
            System.err.println("Can not process density from " + densityName + ". Low resolution");
            errorCode = 5;
            return false;
        }

        File dir = new File("drawable-" + densityName);
        //noinspection ResultOfMethodCallIgnored
        dir.mkdir();
        if (!dir.isDirectory() || !dir.exists()) {
            System.err.println("Error create directory " + dir.getName());
            errorCode = 7;
            return false;
        }

        BufferedImage scaledImage = createResizedCopy(image, targetSize, targetSize, false);
        File targetFile = new File(dir, targetFileName);
        try {
            ImageIO.write(scaledImage, "png", targetFile);
        } catch (IOException e) {
            System.err.println("Error writing to file " + targetFile.getPath());
            errorCode = 8;
            return false;
        }

        return true;
    }

    private BufferedImage createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight, boolean preserveAlpha) {
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = null;

        try {
            g = scaledBI.createGraphics();
            if (preserveAlpha) {
                g.setComposite(AlphaComposite.Src);
            }
            g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        } finally {
            if (g != null) {
                g.dispose();
            }
        }

        return scaledBI;
    }
}