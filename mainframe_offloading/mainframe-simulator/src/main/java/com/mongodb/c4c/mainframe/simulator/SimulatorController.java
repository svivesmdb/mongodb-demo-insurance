package com.mongodb.c4c.mainframe.simulator;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@CrossOrigin
@RestController
public class SimulatorController {

    private final static Logger LOGGER = Logger.getLogger(SimulatorController.class.getName());

    @Autowired
    private SimulatorProperties properties;

    @Autowired
    private DirectoryLister directoryLister;

    //GET example.com/policies // returns all policies
    //GET example.com/policies?type=motor // returns only motor policies
    //GET example.com/policies?type=home // returns only home policies
    @RequestMapping(value = "/policies", method = RequestMethod.GET)
    public List<JSONObject> getAllPolicies(HttpServletResponse response,
            @RequestParam(value = "type", defaultValue = "all") String type,
            @RequestParam(value = "start", defaultValue = "0") String start,
            @RequestParam(value = "limit", defaultValue = "100") Integer limit) {

        LOGGER.info("Fetching Policies :" + type);
        List<JSONObject> contracts = new ArrayList<JSONObject>();
        JSONParser parser = new JSONParser();
        List<String> dirs = new ArrayList<String>();
        if (type.compareToIgnoreCase("all") == 0 || type.compareToIgnoreCase("home") == 0
                || type.compareToIgnoreCase("motor") == 0) {
            dirs.add(properties.getBase() + File.separator + "policy");
        }

        File[] files = directoryLister.getFiles(dirs, start, limit);
        try {
            for (File f : files) {
                Object obj = parser.parse(new FileReader(f));
                contracts.add((JSONObject) obj);//Might need later one some logic to differenciate home/car
                response.setContentType("application/json");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return contracts;

    }

    //GET example.com/policies/PC_000001 // return details of policy PC_000001
    @RequestMapping(value = "/policies/{policyID}", method = RequestMethod.GET)
    public JSONObject getPolycInsurances(HttpServletResponse response, @PathVariable("policyID") String policyID) {
        LOGGER.info("Fetching Policy :" + policyID);
        JSONObject policy = new JSONObject();
        JSONParser parser = new JSONParser();
        String strFile = properties.getBase() + File.separator + "policy" + File.separator + policyID;
        try {
            File jsonFile = null;
            if (new File(strFile + ".json").exists())
                jsonFile = new File(strFile + ".json");
            else
                return policy;
            Object obj = parser.parse(new FileReader(jsonFile));
            policy = (JSONObject) obj;
            response.setContentType("application/json");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return policy;

    }

    //GET example.com/policies/PC_000001/claims // returns all claims issued against a particular policy
    @RequestMapping(value = "/policies/{policyID}/claims", method = RequestMethod.GET)
    public ResponseEntity<List<JSONObject>> getClaims(HttpServletResponse response,
            @PathVariable("policyID") String policyID, @RequestParam(value = "type") String type,
            @RequestParam(value = "start", defaultValue = "0") String start,
            @RequestParam(value = "limit", defaultValue = "100") Integer limit) {
        LOGGER.info("Fetching claims for Policy :" + policyID);
        List<JSONObject> claims = new ArrayList<JSONObject>();
        JSONParser parser = new JSONParser();
        String claimsDir;
        if (type.compareToIgnoreCase("home") == 0 || type.compareToIgnoreCase("motor") == 0)
            claimsDir = properties.getBase() + File.separator + "policy" + File.separator + policyID;
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        ArrayList<String> dirs = new ArrayList<String>() {
            {
                add(claimsDir);
            }
        };
        File[] files = directoryLister.getFiles(dirs, start, limit);
        try {
            for (File f : files) {
                Object obj = parser.parse(new FileReader(f));
                claims.add((JSONObject) obj);
                response.setContentType("application/json");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity(claims, new HttpHeaders(), HttpStatus.OK);

    }

    //GET example.com/customers // returns all customers
    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public List<JSONObject> getCustomers(HttpServletResponse response,
            @RequestParam(value = "start", defaultValue = "0") String start,
            @RequestParam(value = "limit", defaultValue = "100") Integer limit) {

        List<JSONObject> contracts = new ArrayList<JSONObject>();
        JSONParser parser = new JSONParser();
        ArrayList<String> dirs = new ArrayList<String>() {
            {
                add(properties.getBase());
            }
        };
        File[] files = directoryLister.getFiles(dirs, start, limit);
        LOGGER.info("Found " + files.length + " files.");
        try {
            for (File f : files) {
                Object obj = parser.parse(new FileReader(f));
                contracts.add((JSONObject) obj);
                response.setContentType("application/json");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return contracts;

    }

    //POST example.com/policies?type=motor // create new policy
    @SuppressWarnings("unchecked")
    @PostMapping(value = "/policies")
    public ResponseEntity<?> createPolicy(@RequestBody MultiValueMap<String, String> formData,
            @RequestParam(value = "type") String type) {

        LOGGER.info("Creating a new Policy: " + type);
        LOGGER.info("Params: " + formData.toString());

        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");

        //Create the JSON structure
        String policyId;
        JSONObject newPolicy = new JSONObject();
        for (String str : formData.keySet()) {
            // Proper Handling of special fields
            if (str.equals("max_coverd")) {
                try {
                    newPolicy.put(str, Double.parseDouble(formData.getFirst(str)));
                } catch (NumberFormatException nfe) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Could not parse max_coverd - not a valid decimal number, e.g. 1234.56");
                }
            }
            //        		else if (str.equals("cover_start")) {
            //        			try {
            //        				newPolicy.put(str, sdf.parse(formData.getFirst(str)));
            //        			}
            //        			catch(ParseException pe) {
            //        				return ResponseEntity
            //        			            .status(HttpStatus.BAD_REQUEST)
            //        			            .body("Could not parse cover_start - not a valid date, e.g. 2018-12-24");
            //        			}
            //        		}
            else {
                newPolicy.put(str, formData.getFirst(str));
            }
        }
        //newPolicy.remove("type");
        String filename;
        //Base
        File policyDir = new File(properties.getBase() + File.separator + "policy");
        if (!policyDir.exists())
            policyDir.mkdirs();

        //CDC
        File cdcDir = new File(properties.getCdc() + File.separator + "policy");
        if (!cdcDir.exists())
            cdcDir.mkdirs();

        policyId = directoryLister.getNextPolicyID(properties.getBase() + File.separator + "policy");
        if (type.compareToIgnoreCase("home") == 0 || type.compareToIgnoreCase("motor") == 0)
            filename = properties.getBase() + File.separator + "policy" + File.separator + policyId;
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        newPolicy.put("policy_id", policyId);
        try {
            FileWriter file = new FileWriter(filename + ".json");
            file.write(newPolicy.toJSONString());
            file.flush();
            FileWriter cdcfile = new FileWriter(cdcDir.getAbsoluteFile() + File.separator + policyId + ".json");
            cdcfile.write(newPolicy.toJSONString());
            cdcfile.flush();
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(newPolicy, new HttpHeaders(), HttpStatus.OK);
    }

    //POST example.com/policies/PC_000001 // create new claim against policy
    @SuppressWarnings("unchecked")
    @PostMapping(value = "/policies/{policyID}")
    public ResponseEntity<?> createnewClaim(@RequestBody MultiValueMap<String, String> formData,
            @RequestParam(value = "type") String type, @PathVariable("policyID") String policyID) {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");

        LOGGER.info("Creating a new Claim for policy:" + policyID);
        JSONObject newClaim = new JSONObject();
        for (String str : formData.keySet()) {
            if (str.equals("claim_amount") || str.equals("settled_amount")) {
                try {
                    newClaim.put(str, Double.parseDouble(formData.getFirst(str)));
                } catch (NumberFormatException nfe) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Could not parse max_coverd - not a valid decimal number, e.g. 1234.56");
                }
            }
            //	    		else if (str.equals("claim_date") || str.equals("settled_date")) {
            //	    			try {
            //	    				newClaim.put(str, sdf.parse(formData.getFirst(str)));
            //	    			}
            //	    			catch(ParseException pe) {
            //	    				return ResponseEntity
            //	    			            .status(HttpStatus.BAD_REQUEST)
            //	    			            .body("Could not parse cover_start - not a valid date, e.g. 2018-12-24");
            //	    			}
            //	    		}
            else {
                newClaim.put(str, formData.getFirst(str));
            }
        }
        newClaim.remove("type");
        newClaim.put("policy_id", policyID);
        //Create a directory with the policy ID
        File claimDir = new File(properties.getBase() + File.separator + "claim");
        if (!claimDir.exists())
            claimDir.mkdirs();
        //CDC
        File cdcDir = new File(properties.getCdc() + File.separator + "claim");
        if (!cdcDir.exists())
            cdcDir.mkdirs();

        String claimId = directoryLister.getNextClaimID(properties.getBase() + File.separator + "claim");
        newClaim.put("claim_id", claimId);
        String filename;
        if (type.compareToIgnoreCase("home") == 0 || type.compareToIgnoreCase("motor") == 0)
            filename = properties.getBase() + File.separator + "claim" + File.separator + claimId;
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        //Verify if that policy has already been processed???
        if (!new File(properties.getBase() + File.separator + "policy" + File.separator + policyID + ".json").exists()
                && !new File(properties.getBase() + File.separator + "policy" + File.separator + policyID
                        + ".json.processed").exists())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        try {
            FileWriter file = new FileWriter(filename + ".json");
            file.write(newClaim.toJSONString());
            file.flush();
            file.close();
            FileUtils.copyFile(new File(filename + ".json"),
                    new File(cdcDir.getAbsoluteFile() + File.separator + claimId + ".json"));
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(newClaim, new HttpHeaders(), HttpStatus.OK);
    }

}
