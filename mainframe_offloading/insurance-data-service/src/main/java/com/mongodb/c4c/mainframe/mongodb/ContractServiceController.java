package com.mongodb.c4c.mainframe.mongodb;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class ContractServiceController {

    @Autowired
    private MongoDBRepository  mongoDBRepository;

    //GET example.com/policies?type=motor // returns only motor policies
    //GET example.com/policies?type=home&limit=100 // returns only home policies
    @RequestMapping(value="/policies", method= RequestMethod.GET)
    public ResponseEntity<List<Document>> getAllPolicies(HttpServletResponse response,
                                         @RequestParam(value = "type", defaultValue = "motor") String type,
                                         @RequestParam(value = "limit", defaultValue = "100") Integer limit) {
        List<Document> result;
        if(type.compareToIgnoreCase("motor")==0)
            result =  mongoDBRepository.getContracts("motor",limit);
        else if(type.compareToIgnoreCase("home")==0)
            result =  mongoDBRepository.getContracts("home",limit);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity(result, new HttpHeaders(), HttpStatus.OK);
    }
}
