package com.mongodb.c4c.mainframe.simulator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SimulatorController {

    @Autowired
    private SimulatorProperties properties;


    @RequestMapping("/get")
    public void getFiles(){

    }

    @RequestMapping("/upload")
    public void uploadFile(){

    }
}
