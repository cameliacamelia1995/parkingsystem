package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }
    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }
    @AfterAll
    private static void tearDown(){

    }
    @Test
    public void testParkingACar(){

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        parkingService.processIncomingVehicle();

       //assertEquals(1,parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
        assertFalse(parkingSpot.isAvailable());
    }

    @Test
    @DisplayName("Test pour voir si le vélo est actuellement sauvegardé dans la DB et met sa place indisponible")
    public void testParkingABike(){

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        parkingService.processIncomingVehicle();

        //assertEquals(1,parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
        assertFalse(parkingSpot.isAvailable());
    }

    @Test
    @DisplayName("Test pour voir si le prix et l'heure de sortie du véhicule sont enregistré dans la DB")
    public void testParkingLotExit(){
        //TODO: check that the fare generated and out time are populated correctly in the database
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        Date inTime = new Date();
        ticket.setInTime(inTime);
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ticket.setOutTime(outTime);
        ticketDAO.saveTicket(ticket);

        parkingService.processExitingVehicle();

        assertEquals(true, ticket.getOutTime() != null);
        assertEquals(true, ticket.getPrice() >= 0);




    }

}
