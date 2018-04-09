package com.mongodb.c4c.mainframe.filecdc;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.stereotype.Component;
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

}
