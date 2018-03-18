package com.mongodb.c4c.mainframe.simulator;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("mainframe.repository")
public class SimulatorProperties {

    private String motor;
    private String home;
    private String customer;

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

    public String getCustomer() { return customer; }

    public void setCustomer(String customer) { this.customer = customer;}
}
