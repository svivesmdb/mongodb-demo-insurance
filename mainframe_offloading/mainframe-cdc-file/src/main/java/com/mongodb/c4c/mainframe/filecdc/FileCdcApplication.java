package com.mongodb.c4c.mainframe.filecdc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.support.Transformers;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.expression.Expression;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.mongodb.outbound.MongoDbStoringMessageHandler;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;


@SpringBootApplication
@IntegrationComponentScan
@Component
public class FileCdcApplication {

	private final static Logger LOGGER = Logger.getLogger("Mainframe::CDC");

	public static void main(String[] args) {
		new SpringApplicationBuilder(FileCdcApplication.class)
				.web(false)
				.run(args);
	}

	@Autowired
	private MainframeSourceProperties properties;

	@Autowired
	private MongoDbOutputProperties mongoProperties;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Bean
	@Autowired
	public IntegrationFlow motorFlow() {

		return IntegrationFlows
				.from(s -> s.file(new File(properties.getMotorDirectory()))
								.patternFilter("*.json"),
						e -> e.poller(Pollers.fixedDelay(1000)))
				.transform(Transformers.fileToString())
				//.transform(Transformers.converter(stringToJSON())
				.channel("mongodbMotorChannel")
				.get();
	}

	@Bean
	@Autowired
	public IntegrationFlow homeFlow() {

		return IntegrationFlows
				.from(s -> s.file(new File(properties.getHomeDirectory()))
								.patternFilter("*.json"),
						e -> e.poller(Pollers.fixedDelay(1000)))
				.transform(Transformers.fileToString())
				//.transform(Transformers.converter(stringToJSON())
				.channel("mongodbHomeChannel")
				.get();
	}


	@Bean
	@ServiceActivator(inputChannel = "mongodbMotorChannel")
	public MessageHandler mongoDbSinkMessageHandler() {
		LOGGER.log(Level.INFO, "New Motor policy detected ");
		MongoDbStoringMessageHandler mongoDbMessageHandler = new MongoDbStoringMessageHandler(this.mongoTemplate);
		Expression collectionExpression = new LiteralExpression(this.mongoProperties.getMotor());
		mongoDbMessageHandler.setCollectionNameExpression(collectionExpression);
		return mongoDbMessageHandler;
	}

	@Bean
	@ServiceActivator(inputChannel = "mongodbHomeChannel")
	public MessageHandler mongoDbHomeSinkMessageHandler() {
		LOGGER.log(Level.INFO, "New home policy detected ");

		MongoDbStoringMessageHandler mongoDbMessageHandler = new MongoDbStoringMessageHandler(this.mongoTemplate);
		Expression collectionExpression = collectionExpression = new LiteralExpression(this.mongoProperties.getHome());
		mongoDbMessageHandler.setCollectionNameExpression(collectionExpression);
		return mongoDbMessageHandler;
	}


}
