package xyz.moonrabbit.copytree.util;

import org.apache.commons.cli.*;

public class CliHelper {

    private static final String DIRECTORY = "directory";
    private static final String PREFIX = "prefix";
    private static final String LOGGING = "logging";
    private static final String OUTPUT = "output";
    private static final String PROJECT_DIR = "project-dir";
    private static final String REMOVE_SEARCH_RESULTS = "remove-search-results";
    private static final String BINARY_SEARCH = "binary-search";
    private static final String NO_ACTIONS = "no-actions";
    private static final String FILE_NAME = "file-name";
    private static final String KEEP_RELATIVE_PATH = "keep-relative-path";

    private final String[] args;
    private final HelpFormatter formatter = new HelpFormatter();
    private final Options options = createOptions();
    private CommandLine cmd;

    public CliHelper(String[] args) {
        this.args = args;
    }

    public boolean parse() {
        CommandLineParser parser = new DefaultParser();

        try {
            cmd = parser.parse(options, args);
            return true;
        } catch (ParseException e) {
            showError(e.getMessage());
            return false;
        }
    }

    public void showError(String message) {
        Util.err(message);
        formatter.printHelp(Util.getJarFileName(), options, true);
        System.exit(1);
    }

    public String getDirectoryPath() {
        return cmd.getOptionValue(DIRECTORY, ".");
    }

    public String getPrefix() {
        return cmd.getOptionValue(PREFIX);
    }

    public String getLoggingFile() {
        return cmd.getOptionValue(LOGGING);
    }

    public String getOutputDirectory() {
        return cmd.getOptionValue(OUTPUT, "output");
    }

    public String getProjectDirectory() {
        return cmd.getOptionValue(PROJECT_DIR);
    }

    public boolean isRemoveSearchResults() {
        return cmd.hasOption(REMOVE_SEARCH_RESULTS);
    }

    public boolean isBinarySearch() {
        return cmd.hasOption(BINARY_SEARCH);
    }

    public boolean isNoActions() {
        return cmd.hasOption(NO_ACTIONS);
    }

    public String getFileName() {
        return cmd.getOptionValue(FILE_NAME);
    }

    public boolean isKeepRelativePath() {
        return cmd.hasOption(KEEP_RELATIVE_PATH);
    }

    private Options createOptions() {
        Options options = new Options();

//        Option cmd = new Option("c", COMMAND, true, "Command: summary");
//        cmd.setRequired(true);
//        cmd.setType(String.class);
//        options.addOption(cmd);

        Option dir = new Option("d", DIRECTORY, true, "Source directory to search (default current directory)");
        dir.setRequired(true);
        dir.setType(String.class);
        options.addOption(dir);

        Option prefix = new Option("p", PREFIX, true, "Directory prefix");
        prefix.setType(String.class);
        options.addOption(prefix);

        Option logging = new Option("l", LOGGING, true, "Logging to file too");
        logging.setType(String.class);
        options.addOption(logging);

        Option output = new Option("o", OUTPUT, true, "Output directory to generate results");
        output.setType(String.class);
        options.addOption(output);

        Option projectDir = new Option("pd", PROJECT_DIR, true, "Directory with project");
        projectDir.setType(String.class);
        options.addOption(projectDir);

        Option removeSearchResults = new Option("rsr", REMOVE_SEARCH_RESULTS, false, "Remove searched results");
        options.addOption(removeSearchResults);

        Option binarySearch = new Option("bs", BINARY_SEARCH, false, "Enable binary search in file, otherwise search as text files");
        options.addOption(binarySearch);

        Option noActions = new Option("na", NO_ACTIONS, false, "Nothing did, only output to console/file");
        options.addOption(noActions);

        Option fileName = new Option("fn", FILE_NAME, true, "File name from resources");
        fileName.setType(String.class);
        options.addOption(fileName);

        Option keepRelativePath = new Option("krp", KEEP_RELATIVE_PATH, false, "Keep relative paths on coping, otherwise copy all files to one directory with auto renaming");
        options.addOption(keepRelativePath);

        return options;
    }
}