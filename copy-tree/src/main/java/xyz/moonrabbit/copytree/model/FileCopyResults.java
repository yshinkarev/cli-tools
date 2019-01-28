package xyz.moonrabbit.copytree.model;

public class FileCopyResults {

    private final FileCopyResult[] copyResults;
    private final int totalErrors;
    private final String commonPrefixPath;

    public FileCopyResults(FileCopyResult[] copyResults, int totalErrors, String commonPrefixPath) {
        this.copyResults = copyResults;
        this.totalErrors = totalErrors;
        this.commonPrefixPath = commonPrefixPath;
    }

    public FileCopyResult[] getCopyResults() {
        return copyResults;
    }

    public int getTotalErrors() {
        return totalErrors;
    }

    public String getCommonPrefixPath() {
        return commonPrefixPath;
    }
}