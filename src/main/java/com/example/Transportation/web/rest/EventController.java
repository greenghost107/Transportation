package com.example.Transportation.web.rest;

import com.example.Transportation.domain.*;
import com.example.Transportation.exception.SpringException;
import com.example.Transportation.service.EventService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.DiscriminatorValue;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Created by Michael on 6/5/2017.
 */
@RestController
@RequestMapping(value = "/events")
public class EventController {
    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    @Autowired
    EventService eventService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Event>> showAllEvents() {
        return Optional.ofNullable(eventService.findAllEvents())
                .map(driver -> new ResponseEntity<>(driver, HttpStatus.OK))
                .orElseThrow(() -> new SpringException("no Events In Drivers DB"));

    }

    //	Show events of type X and driver Y (x,y are parameters)
    @RequestMapping(value = "/getspecific", method = RequestMethod.GET)
    public ResponseEntity<List<Event>> showEventsOfTypeXandDriverY(@RequestParam(name = "event") String eventType, @RequestParam(name = "driver_name") String driver_name) {
        return Optional.ofNullable(eventService.findEventsByEventTypeAndDriver(eventType, driver_name))
                .map(event -> new ResponseEntity<>(event, HttpStatus.OK))
                .orElseThrow(() -> new SpringException("no " + eventType + " were found in Events DB"));
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<List<Event>> getAllEventsFromType(@RequestParam(name = "event") String eventType) {
        return Optional.ofNullable(eventService.findAllEventsOfTypes(eventType))
                .map(event -> new ResponseEntity<>(event, HttpStatus.OK))
                .orElseThrow(() -> new SpringException("no " + eventType + " were found in Events DB"));
    }


    @RequestMapping(value = "/ticket", method = RequestMethod.POST)
    public ResponseEntity<Ticket> addTicket(@RequestParam long driver_id,
                                            @RequestParam long vehicle_id,
                                            @RequestParam String city,
                                            @RequestParam String street,
                                            @RequestParam float fine,
                                            @RequestParam String reasonForTicket) {
        return Optional.ofNullable(eventService.addTicket(driver_id, vehicle_id, city, street, fine, reasonForTicket))
                .map(ticket -> new ResponseEntity<>(ticket, HttpStatus.OK))
                .orElseThrow(() -> new SpringException("Couldn't add Ticket"));
    }


    @RequestMapping(value = "/trafficticket", method = RequestMethod.POST)
    public ResponseEntity<TrafficTicket> addTrafficTicket(@RequestParam long driver_id,
                                                          @RequestParam long vehicle_id,
                                                          @RequestParam String city,
                                                          @RequestParam String street,
                                                          @RequestParam float fine,
                                                          @RequestParam String reasonForTicket,
                                                          @RequestParam String cause) {
        return Optional.ofNullable(eventService.addTrafficTicket(driver_id, vehicle_id, city, street, fine, reasonForTicket, cause))
                .map(trafficTicket -> new ResponseEntity<>(trafficTicket, HttpStatus.OK))
                .orElseThrow(() -> new SpringException("Couldn't add Ticket"));
    }

    @RequestMapping(value = "/Accident", method = RequestMethod.POST)
    public ResponseEntity<Accident> addAccident(@RequestParam long driver_id,
                                                @RequestParam long vehicle_id,
                                                @RequestParam String city,
                                                @RequestParam String street,
                                                @RequestParam String driverLicense,
                                                @RequestParam String driverName,
                                                @RequestParam String otherDriverId,
                                                @RequestParam String carType,
                                                @RequestParam String carColor,
                                                @RequestParam String carNumber,
                                                @RequestParam String insuranceCompany)
    {
        return Optional.ofNullable(eventService.addNewAccident(driver_id, vehicle_id, city, street, driverLicense,driverName,otherDriverId,carType,carColor,carNumber,insuranceCompany))
                .map(trafficTicket -> new ResponseEntity<>(trafficTicket, HttpStatus.OK))
                .orElseThrow(() -> new SpringException("Couldn't add Accident"));
    }

    @RequestMapping(value = "/{eventId}",method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteDriverFromTraining(@PathVariable("eventId") long eventId)
    {
        return new ResponseEntity<>(eventService.deleteEventForDriver(eventId),HttpStatus.OK);
    }

    @RequestMapping(value = "/breakdown",method = RequestMethod.GET)
    public ResponseEntity<HashMap<String,Float>> calcEventBreakdown(){
        return Optional.ofNullable(eventService.calcBreakdown())
                .map(trafficTicket -> new ResponseEntity<>(trafficTicket, HttpStatus.OK))
                .orElseThrow(() -> new SpringException("Couldn't add Accident"));
    }



}
