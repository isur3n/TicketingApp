package com.walmart.surendra.ticketingApp.ticketingAPI.app;

import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Ticcket API's starting class
 */
@SpringBootApplication
@ComponentScan("com.walmart.surendra.ticketingApp.*")
public class TicketingAPIApp implements CommandLineRunner {
	private static final Logger LOGGER = Logger.getLogger(TicketingAPIApp.class);

	public static void main(String[] args) throws Exception {
		SpringApplication app = new SpringApplication(TicketingAPIApp.class);
		app.run(args);
	}

	public void run(String... args) throws Exception {
		try {
			LOGGER.info("Ticketing application is up & running.");
		} catch (Exception e) {
			LOGGER.fatal(e);
			System.exit(-1);
		}
	}
}
