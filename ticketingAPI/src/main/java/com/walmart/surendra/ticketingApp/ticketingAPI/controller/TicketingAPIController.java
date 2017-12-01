package com.walmart.surendra.ticketingApp.ticketingAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.surendra.ticketingApp.ticketingAPI.apiResponses.NumSeatsResponse;
import com.walmart.surendra.ticketingService.interfaces.TicketService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Surendra Raut on 11/30/2017.
 */
@Controller
@RequestMapping("/ticketingAPI")
public class TicketingAPIController {

    private static final Logger LOGGER = Logger.getLogger(TicketingAPIController.class);

    private final static ObjectMapper jSonMapper = new ObjectMapper();

    @Autowired
    TicketService ts;

    @RequestMapping(value="/numSeatsAvailable/", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> numSeatsAvailable() {

        ResponseEntity<String> response = null;
        try {
            NumSeatsResponse nsr = new NumSeatsResponse();
            nsr.setNumSeats(ts.numSeatsAvailable());
            response = new ResponseEntity<String>(jSonMapper.writeValueAsString(nsr), HttpStatus.OK);
            LOGGER.info("Inside the controller: seatsAvailable = " + nsr.getNumSeats());
        } catch (Exception e) {
            response = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            LOGGER.error(e);
        }
        return response;
    }

    @RequestMapping(value="/findAndHoldSeats/", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findAndHoldSeats(@RequestBody String jsonData) {

        ResponseEntity<String> response = null;
        try {
            Map<String, Integer> responseMap = new HashMap<String, Integer>();
        } catch (Exception e) {
            response = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            LOGGER.error(e);
        }
        return response;
    }

    @RequestMapping(value="/reserveSeats/", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> reserveSeats(@RequestBody String jsonData) {

        ResponseEntity<String> response = null;
        try {
            Map<String, String> responseMap = new HashMap<String, String>();
        } catch (Exception e) {
            response = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            LOGGER.error(e);
        }
        return response;
    }
}

