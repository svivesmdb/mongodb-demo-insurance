package com.mongodb.c4c.mainframe.filecdc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.Expression;
import org.springframework.util.StringUtils;

@Configuration
@ConfigurationProperties("mongodb.collection")
public class MongoDbOutputProperties {

    private String motor;
    private String home;

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }
}
