package com.mongodb.c4c.mainframe.filecdc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.Expression;
import org.springframework.util.StringUtils;

@Configuration
@ConfigurationProperties("mongodb")
public class MongoDbOutputProperties {

    /**
     * The MongoDB collection to store data
     */
    private String collection;

    /**
     * The SpEL expression to evaluate MongoDB collection
     */
    private Expression collectionExpression;

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getCollection() {
        return this.collection;
    }

    public void setCollectionExpression(Expression collectionExpression) {
        this.collectionExpression = collectionExpression;
    }

    public Expression getCollectionExpression() {
        return collectionExpression;
    }

}
