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

    private String homeDirectory = DEFAULT_DIR;
    private String motorDirectory = DEFAULT_DIR;

    public String getHomeDirectory() {
        return homeDirectory;
    }

    public void setHomeDirectory(String homeDirectory) {
        this.homeDirectory = homeDirectory;
    }

    public String getMotorDirectory() {
        return motorDirectory;
    }

    public void setMotorDirectory(String motorDirectory) {
        this.motorDirectory = motorDirectory;
    }
}
