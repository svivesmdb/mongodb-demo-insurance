package com.mongodb.c4c.mainframe.filecdc;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.file.DirectoryScanner;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.RecursiveDirectoryScanner;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.RegexPatternFileListFilter;
import org.springframework.messaging.Message;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

@Configuration
public class FilePollingIntegrationFlow {

    private final static Logger LOGGER = Logger.getLogger("Mainframe::CDC");

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    public File inboundReadDirectory;

    @Autowired
    private ApplicationContext applicationContext;


    @Bean
    public IntegrationFlow inboundFileIntegration(TaskExecutor taskExecutor,
                                                  MessageSource<File> fileReadingMessageSource) {
        return IntegrationFlows.from(fileReadingMessageSource,
                c -> c.poller(Pollers.fixedDelay(1000)
                        .taskExecutor(taskExecutor)
                        .maxMessagesPerPoll(5)))
                .channel("mongodbCustomerChannel")
                .get();
    }
    @ServiceActivator(inputChannel = "mongodbCustomerChannel")
    public void mongoDbCustomerMessageHandler(Message<File> msg) {
        File payload = (File) msg.getPayload();
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(payload));
            JSONObject json = (JSONObject) obj;
            if(json.containsKey("claim_date")){  //Is is a claim?
                LOGGER.info("New Claim detected: " + payload);
                String policyId = (String)json.get("policy_id");
                json.remove("policy_id");
                json.put("last_change", new Date());
                mongoTemplate.updateFirst(Query.query(Criteria.where("home_insurance.policy_id").is(policyId)), new Update().push("home_insurance.$.claim", json), "customer");
                payload.renameTo(new File(payload.getAbsoluteFile()+".processed"));

            } else if(json.containsKey("cover_start")){   // Is is a policy?
                LOGGER.info("New Policy detected: " + payload);
                json.put("claim", new ArrayList<>());
                String customerID = (String)json.get("customer_id");
                json.remove("customer_id");
                mongoTemplate.updateFirst(Query.query(Criteria.where("customer_id").is(customerID)), new Update().push("home_insurance", json), "customer");
                mongoTemplate.updateFirst(Query.query(Criteria.where("customer_id").is(customerID)), new Update().set("last_change_home_policy", new Date()), "customer");
                payload.renameTo(new File(payload.getAbsoluteFile()+".processed"));

            } else if(json.containsKey("first_name")){ //Is is a customer?
                LOGGER.info("New Customer detected: " + json);
                json.put("home_insurance", new ArrayList<>());
                json.put("car_insurance", new ArrayList<>());
                mongoTemplate.save(json, "customer");
                payload.renameTo(new File(payload.getAbsoluteFile()+".processed"));
            }else {
                LOGGER.warning("Invalid json found...");
                payload.renameTo(new File(payload.getAbsoluteFile()+".invalid"));
            }

        }catch(Exception ex ){
            ex.printStackTrace();
            payload.renameTo(new File(payload.getAbsoluteFile()+".invalid"));
        }

    }

    @Bean
    TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        return taskExecutor;
    }



    @Bean
    public FileReadingMessageSource fileReadingMessageSource(DirectoryScanner directoryScanner) {
        FileReadingMessageSource source = new FileReadingMessageSource();
        source.setDirectory(this.inboundReadDirectory);
        source.setScanner(directoryScanner);
        source.setAutoCreateDirectory(true);
        return source;
    }


    @Bean
    public DirectoryScanner directoryScanner() {
        DirectoryScanner scanner = new RecursiveDirectoryScanner();
        CompositeFileListFilter filter = new CompositeFileListFilter<>(
                Arrays.asList(new AcceptOnceFileListFilter<>(),
                        new RegexPatternFileListFilter("([^\\s]+(\\.(?i)(json))$)"))
        );
        scanner.setFilter(filter);
        return scanner;
    }
}
