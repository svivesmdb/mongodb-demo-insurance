package com.mongodb.c4c.mainframe.filecdc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class FilePollingConfiguration {

    @Bean(name="inboundReadDirectory")
    public File inboundReadDirectory(@Value("${inbound.read.path}") String path) {
        return makeDirectory(path);
    }

    private File makeDirectory(String path) {
        File file = new File(path);
        file.mkdirs();
        return file;
    }
}
