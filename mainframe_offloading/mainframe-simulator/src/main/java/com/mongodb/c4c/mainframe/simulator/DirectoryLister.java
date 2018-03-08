package com.mongodb.c4c.mainframe.simulator;

import org.springframework.stereotype.Service;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.comparator.NameFileComparator;


@Service
public class DirectoryLister {

    private final static Logger LOGGER = Logger.getLogger(DirectoryLister.class.getName());


    public File[] getFiles(String dir, String start, int limit){
        LOGGER.log(Level.INFO, "Looking for files in directory " + dir);
        try {
            File directory = new File(dir);
            File[] files = directory.listFiles((d, s) -> {
                return s.toLowerCase().endsWith("json");
            });

            if(files!=null) {
                Arrays.sort(files, NameFileComparator.NAME_INSENSITIVE_COMPARATOR);
                //Look for the index
                if(start!=null) {
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].getName().compareToIgnoreCase(start) == 0)
                            System.out.println("Position = " + i);
                    }
                }
                // Limit the result
                int filesCount = files.length;
                limit = limit > filesCount ? filesCount : limit;
                return Arrays.copyOfRange(files,0,limit);
            } else {
                // return empty array in case no files have been found
                return new File[0];
            }
        }
        catch(Exception ex){ ex.printStackTrace(); return null;}
    }


}
