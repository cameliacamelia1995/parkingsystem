package com.parkit.parkingsystem;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test
    public void calculateFareCar(){
        LocalDateTime inTime = LocalDateTime.now();
        LocalDateTime outTime = inTime.plusHours(1);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(Date.from(inTime.toInstant(ZoneOffset.UTC)));
        ticket.setOutTime(Date.from(outTime.toInstant(ZoneOffset.UTC)));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareBike(){
        LocalDateTime inTime = LocalDateTime.now();
        LocalDateTime outTime = inTime.plusHours(1);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(Date.from(inTime.toInstant(ZoneOffset.UTC)));
        ticket.setOutTime(Date.from(outTime.toInstant(ZoneOffset.UTC)));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareUnkownType(){
        LocalDateTime inTime = LocalDateTime.now();
        LocalDateTime outTime = inTime.plusHours(1);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(Date.from(inTime.toInstant(ZoneOffset.UTC)));
        ticket.setOutTime(Date.from(outTime.toInstant(ZoneOffset.UTC)));
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithFutureInTime(){
        LocalDateTime inTime = LocalDateTime.now();
        LocalDateTime outTime = inTime.plusHours(1);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(Date.from(inTime.toInstant(ZoneOffset.UTC)));
        ticket.setOutTime(Date.from(outTime.toInstant(ZoneOffset.UTC)));
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
        LocalDateTime inTime = LocalDateTime.now();
        LocalDateTime outTime = inTime.minusMinutes(45);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(Date.from(inTime.toInstant(ZoneOffset.UTC)));
        ticket.setOutTime(Date.from(outTime.toInstant(ZoneOffset.UTC)));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime(){
        LocalDateTime inTime = LocalDateTime.now();
        LocalDateTime outTime = inTime.minusMinutes(45);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(Date.from(inTime.toInstant(ZoneOffset.UTC)));
        ticket.setOutTime(Date.from(outTime.toInstant(ZoneOffset.UTC)));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (0.75 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTime(){
        LocalDateTime inTime = LocalDateTime.now();
        LocalDateTime outTime = inTime.plusDays(1);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(Date.from(inTime.toInstant(ZoneOffset.UTC)));
        ticket.setOutTime(Date.from(outTime.toInstant(ZoneOffset.UTC)));
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

}
