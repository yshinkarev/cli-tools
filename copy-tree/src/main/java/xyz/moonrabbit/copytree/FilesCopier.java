package xyz.moonrabbit.copytree;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import xyz.moonrabbit.copytree.model.FileCopyResult;
import xyz.moonrabbit.copytree.model.FileCopyResults;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilesCopier {

    private final File rootDir;
    private final String prefix;

    public FilesCopier(File rootDir, String prefix) {
        this.rootDir = rootDir;
        this.prefix = prefix;
    }

    public FileCopyResults copyFiles(File outputDir, String fileName, boolean keepRelativePath) {
        List<String> filePaths = getFilePaths(fileName);
        String commonPrefixPath = StringUtils.substringBeforeLast(StringUtils.getCommonPrefix(filePaths.toArray(new String[0])), File.separator);

        List<FileCopyResult> copyResults = new ArrayList<>(filePaths.size());
        int totalErrors = 0;
        String suffix = FilenameUtils.getExtension(fileName);
        if (StringUtils.isNotEmpty(suffix)) {
            suffix = "." + suffix;
        }

        for (String filePath : filePaths) {
            File srcFile = new File(filePath, fileName);
            boolean success = true;
            File destFile;

            if (keepRelativePath) {
                File dir = new File(outputDir, StringUtils.removeStart(filePath, commonPrefixPath));
                destFile = new File(dir, fileName);

                try {
                    FileUtils.copyFileToDirectory(srcFile, dir, true);
                } catch (IOException e) {
                    success = false;
                }
            } else {
                String name = StringUtils.removeStart(filePath, commonPrefixPath);
                destFile = new File(outputDir, StringUtils.strip(name, File.separator).replace(File.separator, "_") + suffix);

                try {
                    FileUtils.copyFile(srcFile, destFile, true);
                } catch (IOException e) {
                    success = false;
                }
            }

            copyResults.add(new FileCopyResult(new File(filePath, fileName).getPath(), destFile.getPath(), success));

            if (!success) {
                totalErrors++;
            }
        }

        return new FileCopyResults(copyResults.toArray(new FileCopyResult[0]), totalErrors, commonPrefixPath);
    }

    private List<String> getFilePaths(String fileName) {
        List<String> ret = new ArrayList<>();
        search(ret, rootDir, fileName.toLowerCase(), 0);
        return ret;
    }

    private void search(List<String> results, File dir, String fileName, int level) {
        File[] files = dir.listFiles(pathname -> (
                pathname.isDirectory() && (level > 0 || pathname.getName().startsWith(prefix)))
                || (pathname.isFile() && pathname.getName().toLowerCase().equals(fileName))
        );

        if (files == null || files.length == 0) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                search(results, file, fileName, level + 1);
            } else if (file.isFile()) {
                results.add(file.getParent());
            }
        }
    }
}