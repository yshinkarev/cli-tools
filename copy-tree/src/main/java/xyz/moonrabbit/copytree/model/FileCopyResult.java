package xyz.moonrabbit.copytree.model;

public class FileCopyResult {

    private final String from;
    private final String to;
    private final boolean success;

    public FileCopyResult(String from, String to, boolean success) {
        this.from = from;
        this.to = to;
        this.success = success;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public boolean isSuccess() {
        return success;
    }
}