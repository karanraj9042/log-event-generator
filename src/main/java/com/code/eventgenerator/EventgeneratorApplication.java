package com.code.eventgenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class EventgeneratorApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(EventgeneratorApplication.class);


	public static void main(String[] args) {
		SpringApplication.run(EventgeneratorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info(
				"Application started with command-line arguments: {} ",args);
		if(args.length == 0){
			logger.error("Event Log file path not given.");
			System.exit(1);
		}

		String filePath = args[0];
		Path eventFilePath = Paths.get(filePath);
		if (!eventFilePath.toFile().exists()) {
			if(logger.isErrorEnabled()){
				logger.error(String.format("Event Log file not exists for the provided path: %s" , filePath));
			}
			System.exit(2);
		}
		System.setProperty("eventFilePath", eventFilePath.toString());

	}
}
