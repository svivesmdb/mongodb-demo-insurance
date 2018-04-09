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

@Component
public class MongoDBRepository {

    private final static Logger LOGGER = Logger.getLogger(MongoDBRepository.class.getName());

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Document> getContracts(String collectionName, int limit){

        MongoCollection<Document> collection = mongoTemplate.getDb().getCollection(collectionName);
        List<Document> docs = IteratorUtils.toList(collection.find().limit(limit).iterator());
        return docs;
    }

}
