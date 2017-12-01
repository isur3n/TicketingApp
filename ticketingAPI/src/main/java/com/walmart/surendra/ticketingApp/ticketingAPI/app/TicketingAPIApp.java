package com.walmart.surendra.ticketingApp.ticketingAPI.app;

import com.walmart.surendra.ticketingService.bookingAlgorithms.simpleBooking.domain.Show;
import com.walmart.surendra.ticketingService.bookingAlgorithms.simpleBooking.service.TicketServiceImpl;
import com.walmart.surendra.ticketingService.util.PropUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.walmart.surendra.ticketingService.interfaces.TicketService;

/**
 * Ticcket API's starting class
 */
@SpringBootApplication
public class TicketingAPIApp implements CommandLineRunner {
	private static final Logger LOGGER = Logger.getLogger(TicketingAPIApp.class);

	/**
	 * This can be autowired and seat creation can be deferred at later point
	 */
	Show movieShow;

	TicketService ts;

	public TicketingAPIApp() {
		int rows = Integer.valueOf(PropUtils.getAppPropertyValue(PropUtils.SHOW_ROWS, true));
		int cols = Integer.valueOf(PropUtils.getAppPropertyValue(PropUtils.SHOW_COLS, true));

		movieShow = new Show(rows, cols);
		ts = new TicketServiceImpl(movieShow);
	}
	
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
