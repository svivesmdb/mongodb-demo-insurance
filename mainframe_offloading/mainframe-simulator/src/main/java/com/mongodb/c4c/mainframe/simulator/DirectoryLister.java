package com.mongodb.c4c.mainframe.simulator;

import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.comparator.NameFileComparator;

@Service
public class DirectoryLister {

    private final static Logger LOGGER = Logger.getLogger(DirectoryLister.class.getName());

    public File[] getFiles(List<String> dirs, String start, int limit) {
        LOGGER.log(Level.INFO, "Looking for files in directory " + dirs);
        try {
            //Get the Home policies
            File[] allPolicies = {};
            for (String dir : dirs) {
                File[] policies = new File(dir).listFiles((d, s) -> {
                    return (s.toLowerCase().endsWith("json") || s.toLowerCase().endsWith("json.processed"));
                });
                if (policies != null)
                    allPolicies = (File[]) org.apache.commons.lang3.ArrayUtils.addAll(allPolicies, policies);
            }
            if (allPolicies != null) {
                Arrays.sort(allPolicies, NameFileComparator.NAME_INSENSITIVE_COMPARATOR);
                if (start != null) {
                    for (int i = 0; i < allPolicies.length; i++) {
                        if (allPolicies[i].getName().compareToIgnoreCase(start + ".json") == 0) {
                            allPolicies = Arrays.copyOfRange(allPolicies, i, allPolicies.length);
                            break;
                        }
                    }
                }
                // Limit the result
                int filesCount = allPolicies.length;
                limit = limit > filesCount ? filesCount : limit;
                return Arrays.copyOfRange(allPolicies, 0, limit);
            } else {
                // return empty array in case no files have been found
                LOGGER.log(Level.INFO, "Sorry no file has been found");
                return new File[0];
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public File getFile(String dir, String filename) {
        LOGGER.log(Level.INFO, "Looking for file in directory " + dir);
        try {
            File directory = new File(dir);
            File[] files = directory.listFiles((d, s) -> {
                return s.toLowerCase().endsWith("json");
            });

            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public File[] getFolders(String dir, String filename) {
        LOGGER.log(Level.INFO, "Looking for all sub folder in directory " + dir);
        try {
            File directory = new File(dir);
            File[] directories = directory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File current, String name) {
                    return new File(current, name).isDirectory();
                }
            });
            return directories;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getNextPolicyID(String path) {
        LOGGER.log(Level.INFO, "getNextPolicyID: looking into " + path);
        String policyID = "PC_000000001";
        File[] policies = new File(path).listFiles((d, s) -> {
            return s.toLowerCase().startsWith("pc_") && (s.toLowerCase().endsWith("json"));
        });
        LOGGER.log(Level.INFO, "getNextPolicyID: found " + policies.length + " policies");
        if (policies != null && policies.length > 0) {
            Arrays.sort(policies, NameFileComparator.NAME_INSENSITIVE_COMPARATOR);
            String lastFileNAme = policies[policies.length - 1].getName();
            String lastID = lastFileNAme.substring(3, 12);
            LOGGER.log(Level.INFO, "getNextPolicyID: last assigned ID: " + lastID);
            int str = Integer.parseInt(lastID) + 1;
            int l = String.valueOf(str).length();
            String prefix = "PC_";
            for (int i = l; i < 9; i++)
                prefix += "0";
            policyID = prefix + str;
        }
        LOGGER.log(Level.INFO, "getNextPolicyID: calculated next ID: " + policyID);
        return policyID;
    }

    public String getNextClaimID(String path) {
        String claimID = "CL_000000001";
        File[] claims = new File(path).listFiles((d, s) -> {
            return s.toLowerCase().startsWith("cl_") && (s.toLowerCase().endsWith("json"));
        });
        if (claims != null && claims.length > 0) {
            Arrays.sort(claims, NameFileComparator.NAME_INSENSITIVE_COMPARATOR);
            String lastFileNAme = claims[claims.length - 1].getName();
            String lastID = lastFileNAme.substring(3, 12);
            int str = Integer.parseInt(lastID) + 1;
            int l = String.valueOf(str).length();
            String prefix = "CL_";
            for (int i = l; i < 9; i++)
                prefix += "0";
            claimID = prefix + str;
        }
        return claimID;
    }
}
