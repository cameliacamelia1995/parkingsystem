package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * TicketDAO test will check and update the ticket in the DB.
 */
public class TicketDAOTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @AfterAll
    private static void tearDown() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    @DisplayName("Update the ticket for the car in the DB")
    public void updateTicketCarTest() {
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        ticket.setVehicleRegNumber("ABCDEF");
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setPrice(1);
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        ticket.setInTime(inTime);
        ticketDAO.saveTicket(ticket);


        Date outTime = new Date();
        ticket.setOutTime(outTime);
        ticket.setPrice(1);
       boolean t = ticketDAO.updateTicket(ticket);


        assertTrue(t);
    }
    @Test
    @DisplayName("Update the ticket for the bike in the DB")
    public void updateTicketBikeTest() {
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        ticket.setVehicleRegNumber("ABCDEF");
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setPrice(1);
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        ticket.setInTime(inTime);
        ticketDAO.saveTicket(ticket);


        Date outTime = new Date();
        ticket.setOutTime(outTime);
        ticket.setPrice(1);
        ticketDAO.updateTicket(ticket);

        assertTrue(ticketDAO.updateTicket(ticket));
    }
    @Test
    @DisplayName("Get the ticket in the DB")
    public void getTicketTest() {
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        ticket.setVehicleRegNumber("ABCDEF");
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setPrice(0);
        ticket.setRecurrentUsers(true);
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        ticket.setInTime(inTime);
        ticketDAO.saveTicket(ticket);

        assertNotNull(ticketDAO.getTicket("ABCDEF"));

    }
}