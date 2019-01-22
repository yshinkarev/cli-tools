package xyz.moonrabbit.photorenamer;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Launcher {

    public static void main(String[] args) {
        String source = (args.length == 0) ? "." : args[0];

        System.out.println("Directory: " + source);

        File rootDir = new File(source);
        File[] files = rootDir.listFiles((dir, name) -> {
            name = name.toLowerCase();
            // TODO: append more types.
            return name.endsWith(".jpg");
        });
        if (files == null) {
            files = new File[0];
        } else {
            Arrays.sort(files);
        }

        System.out.println("Files: " + files.length);
        int count = -1;
        Date startDate = new Date(0);
        ErrorLogger error = new ErrorLogger();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy_MM_dd", Locale.getDefault());
        File outputDir = new File(rootDir, "output");
        outputDir.mkdirs();

        for (File file : files) {
            String name = file.getName();
            error.clear();

            Metadata metadata = null;
            try {
                metadata = ImageMetadataReader.readMetadata(file);
            } catch (ImageProcessingException | IOException e) {
                error
                        .tab()
                        .append(e)
                        .line();
            }

            Date date = null;
            if (metadata != null) {
                ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                if (directory == null) {
                    error
                            .tab().append("EXIF not exists")
                            .tab().append("SubIFD directories: ")
                            .append(StreamSupport.stream(metadata.getDirectories().spliterator(), false)
                                    .map(dir -> dir.getName() + ":" + dir.getClass().getSimpleName())
                                    .collect(Collectors.joining(", ", "[", "]")));
                } else {
                    date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                }
            }
            if (date == null) {
                date = new Date(file.lastModified());
            }

            if (date.getDate() == startDate.getDate())
                count++;
            else {
                count = 0;
                startDate = date;
            }

            int newIdx = count;
            File dest;
            String newName;
            do {
                newName = String.format("%s-%02d." + FilenameUtils.getExtension(name), fmt.format(date), newIdx++);
                dest = new File(outputDir, newName);
            }
            while(dest.exists());

            System.out.print(String.format("%s -> %s. Copy: ", name, newName));

            try {
                FileUtils.copyFile(file, dest, true);
                System.out.println("OK");
            } catch (Exception ex) {
                System.out.println("false");
            }

            dest.setLastModified(date.getTime());
        }
    }
}