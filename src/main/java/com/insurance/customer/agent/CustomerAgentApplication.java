package com.insurance.customer.agent;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import com.insurance.customer.agent.service.WatcherService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableAutoConfiguration
@SpringBootApplication
public class CustomerAgentApplication implements CommandLineRunner {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerAgentApplication.class);

	
	 @Autowired
	 public WatcherService watcherService;
	 
	public static void main(String[] args) {
		SpringApplication.run(CustomerAgentApplication.class, args);
		LOGGER.info("trigger to watch the file");
	}
	
	@Override
	public void run(String... strings) throws Exception {
        LOGGER.info("I'm running !");
        new Thread(watcherService, "watcher-service").start();
    }

}
