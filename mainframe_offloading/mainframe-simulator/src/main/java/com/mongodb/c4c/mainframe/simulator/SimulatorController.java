package com.mongodb.c4c.mainframe.simulator;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/contracts")
public class SimulatorController {

    @Autowired
    private SimulatorProperties properties;


    @Autowired
    private DirectoryLister directoryLister;


    @RequestMapping("/get")
    public void getFiles(int limit){

    }

    @RequestMapping(value="/motor", method= RequestMethod.GET)
    public List<JSONObject> getInsurances(HttpServletResponse response, @RequestParam("start") String start, @RequestParam("limit") Integer limit) {

        List<JSONObject> contracts = new ArrayList<JSONObject>();
        JSONParser parser = new JSONParser();
        File[] files = directoryLister.getFiles(properties.getMotor(), null, limit);
        try {
            for (File f : files) {
                Object obj = parser.parse(new FileReader(f));
                contracts.add((JSONObject) obj);
                response.setContentType("application/json");
            }
        }catch (Exception ex){ex.printStackTrace();}

        return contracts;

    }

    /* Are we uploading a file? */
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }
        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(properties.getMotor() + file.getOriginalFilename());
            Files.write(path, bytes);

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Successfully uploaded - " +
                file.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);
    }

}
