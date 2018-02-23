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


@SpringBootApplication
@IntegrationComponentScan
@Component
public class FileCdcApplication {

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
	public IntegrationFlow mainframeReadingFlow() {

		return IntegrationFlows
				.from(s -> s.file(new File(properties.getDirectory()))
								.patternFilter("*.json"),
						e -> e.poller(Pollers.fixedDelay(1000)))
				.transform(Transformers.fileToString())
				.channel("mongodbChannel")
				.get();
	}


	@Bean
	@ServiceActivator(inputChannel = "mongodbChannel")
	public MessageHandler mongoDbSinkMessageHandler() {
		System.out.println("message received:" + mongoProperties.getCollection());
		MongoDbStoringMessageHandler mongoDbMessageHandler = new MongoDbStoringMessageHandler(this.mongoTemplate);
		Expression collectionExpression = this.mongoProperties.getCollectionExpression();
		if (collectionExpression == null) {
			collectionExpression = new LiteralExpression(this.mongoProperties.getCollection());
		}
		mongoDbMessageHandler.setCollectionNameExpression(collectionExpression);
		return mongoDbMessageHandler;
	}


}
