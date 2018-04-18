package com.mongodb.c4c.mainframe.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import java.util.logging.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import static com.mongodb.client.model.Filters.*;


@Component
public class MongoDBRepository {

    private final static String CUSTOMER_COLLECTION_NAME = "customer";

    private final static Logger LOGGER = Logger.getLogger(MongoDBRepository.class.getName());

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Document> getCustomers(int limit){

        MongoCollection<Document> collection = mongoTemplate.getDb().getCollection(CUSTOMER_COLLECTION_NAME);
        List<Document> docs = IteratorUtils.toList(collection.find().limit(limit).iterator());
        return docs;
    }

    public Document getCustomer(String customerId){
        MongoCollection<Document> collection = mongoTemplate.getDb().getCollection(CUSTOMER_COLLECTION_NAME);
        Document doc = collection.find(eq("customer_id", customerId)).first();
        return doc;
    }

}
