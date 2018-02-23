package com.mongodb.c4c.mainframe.filecdc;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.regex.Pattern;
import java.io.File;

@Configuration
@ConfigurationProperties("mainframe")
public class MainframeSourceProperties {

    private static final String DEFAULT_DIR = System.getProperty("java.io.tmpdir") +
            File.separator + "mainframe";

    /**
     * The directory to poll for new files.
     */
    private String directory = DEFAULT_DIR;

    /**
     * Set to true to include an AcceptOnceFileListFilter which prevents duplicates.
     */
    private boolean preventDuplicates = true;

    /**
     * A simple ant pattern to match files.
     */
    private String filenamePattern;

    /**
     * A regex pattern to match files.
     */
    private Pattern filenameRegex;

    public String getDirectory() {
        return this.directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public boolean isPreventDuplicates() {
        return this.preventDuplicates;
    }

    public void setPreventDuplicates(boolean preventDuplicates) {
        this.preventDuplicates = preventDuplicates;
    }

    public String getFilenamePattern() {
        return this.filenamePattern;
    }

    public void setFilenamePattern(String filenamePattern) {
        this.filenamePattern = filenamePattern;
    }

    public Pattern getFilenameRegex() {
        return this.filenameRegex;
    }

    public void setFilenameRegex(Pattern filenameRegex) {
        this.filenameRegex = filenameRegex;
    }


}
