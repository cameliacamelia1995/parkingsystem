package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;
    private Ticket ticket;


    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;
    private ParkingSpot parkingSpot;

    @BeforeEach
    private void setUpPerTest() {
        try {
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            ticket = new Ticket();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    @DisplayName("")
    public void getNextAvailableCarSpot() {
        try {
            ticket = new Ticket();
            parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.getTicket("ABCDEF")).thenReturn(ticket);
            when(ticketDAO.updateTicket(ticket)).thenReturn(true);
            when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processExitingVehicle();
        System.out.println(parkingSpot);

        verify(parkingSpotDAO, Mockito.times(1)).updateParking(parkingSpot);

        assertTrue(parkingSpotDAO.updateParking(parkingSpot));

    }

    @Test
    public void getNextAvailableBikeSpot() {
        try {
            ticket = new Ticket();
            parkingSpot = new ParkingSpot(1, ParkingType.BIKE, true);
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.getTicket("ABCDEF")).thenReturn(ticket);
            when(ticketDAO.updateTicket(ticket)).thenReturn(true);
            when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processExitingVehicle();

        verify(parkingSpotDAO, Mockito.times(1)).updateParking(parkingSpot);
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }

    @Test
    public void processIncomingVehicleTest() {
        try {
            Ticket ticket = new Ticket();
            ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));

            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
            when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processIncomingVehicle();

        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));

    }

    @Test
    public void processExitingVehicleTest() {
        try {
            ticket = new Ticket();
            parkingSpot = new ParkingSpot(1, ParkingType.BIKE, true);
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.getTicket("ABCDEF")).thenReturn(ticket);
            when(ticketDAO.updateTicket(ticket)).thenReturn(true);
            when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        parkingService.processExitingVehicle();

        // On verifie que le parking est MAJ au moins 1fois avec mockito quand parkingspotDAO est appelé
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));
        verify(ticketDAO, Mockito.times(1)).getTicket(anyString());

    }
    @Test
    public void getVehicleTypeCarTest() {

        ParkingService service = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        when(inputReaderUtil.readSelection()).thenReturn(2);

        assertEquals(service.getVehichleType(), parkingService.getVehichleType());

    }

    @Test
    public void getVehicleTypeBikeTest() {

        ParkingService service = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        when(inputReaderUtil.readSelection()).thenReturn(2);

        assertEquals(service.getVehichleType(), parkingService.getVehichleType());

    }
    @Test
    public void getNextParkingIfAvailableTest () {

        parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setParkingSpot(parkingSpot);
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);

        ParkingSpot number = parkingService.getNextParkingNumberIfAvailable();
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(ParkingType.CAR);
        assertEquals(number.getParkingType(), parkingSpot.getParkingType());

    }
}
