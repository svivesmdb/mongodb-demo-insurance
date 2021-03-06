package com.mongodb.c4c.mainframe.mongodb;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value="/v2")
public class ContractServiceController {

    @Autowired
    private MongoDBRepository  mongoDBRepository;

    //GET example.com/v2/customer?limit=100 // returns only home policies
    @RequestMapping(value="/customer", method= RequestMethod.GET)
    public ResponseEntity<List<Document>> getCustomers(HttpServletResponse response,
        @RequestParam(value = "limit", defaultValue = "100") Integer limit) {

        List<Document> result =  mongoDBRepository.getCustomers(limit);

        return new ResponseEntity(result, new HttpHeaders(), HttpStatus.OK);
    }

    //GET example.com/v2/customer?limit=100 // returns only home policies
    @RequestMapping(value="/customer/{customerID}", method= RequestMethod.GET)
    public ResponseEntity<Document> getCustomer(HttpServletResponse response,
        @RequestParam(value = "limit", defaultValue = "100") Integer limit,
        @PathVariable("customerID") String customerID) {

        Document result =  mongoDBRepository.getCustomer(customerID);

        return new ResponseEntity(result, new HttpHeaders(), HttpStatus.OK);
    }
}
