package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class FareCalculatorServiceTest {
    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;


    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
        fareCalculatorService = new FareCalculatorService();
    }

    @Test
    @DisplayName("Calculating parking fare for an hours")
    public void calculateFareCar() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurrentUsers(false);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    @DisplayName("Calculating parking fare for an hour")
    public void calculateFareBike() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurrentUsers(false);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    @DisplayName("Parking type not found ")
    public void calculateFareUnkownType() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setRecurrentUsers(false);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    @DisplayName("Entry time after outing ")
    public void calculateFareBikeWithFutureInTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setRecurrentUsers(false);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }
    @Test
    @DisplayName("Entry time after outing ")
    public void calculateFareCarWithFutureInTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setRecurrentUsers(false);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }
    @Test
    @DisplayName("Price for bike less than 1 hour")
    public void calculateFareBikeWithLessThanOneHourParkingTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurrentUsers(false);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    @DisplayName("Price for car less than 1 hour")
    public void calculateFareCarWithLessThanOneHourParkingTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurrentUsers(false);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(1.13, ticket.getPrice());
    }

    @Test
    @DisplayName("Calculating a price for an entire day in the parking ")
    public void calculateFareCarWithMoreThanADayParkingTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurrentUsers(false);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());

    }

    @Test
    @DisplayName("Calculating a price for an entire day in the parking ")
    public void calculateFareBikeWithMoreThanADayParkingTime() {
        Date inTime = new Date();
        //24 hours parking time should give 24 * parking fare per hour
        inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        System.out.println(inTime);
        ticket.setOutTime(outTime);
        System.out.println(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurrentUsers(false);
        fareCalculatorService.calculateFare(ticket);
        System.out.println(ticket.getPrice());
        assertEquals((24 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    @DisplayName("Calculating price for a car who spend less than 30 minutes")
    public void calculateFareCarWithLessThanThirtyMinute() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (25 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurrentUsers(false);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(0, ticket.getPrice());
    }

    @Test
    @DisplayName("Calculating price for a bike who spend less than 30 minutes")
    public void calculateFareBikeWithLessThanThirtyMinute() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (25 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurrentUsers(false);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(0, ticket.getPrice());
    }

    @Test
    @DisplayName("Calculating price for a car who is recusing users")
    public void calculateFareDiscountCarForRecurringUsers() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (1 * 60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("regTest");
        ticket.setRecurrentUsers(true);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(1.42, ticket.getPrice());
    }

    @Test
    @DisplayName("Calculating price for a bike who is recusing users")
    public void calculateFareDiscountBikeForRecurringUsers() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (1 * 60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("regTest");
        ticket.setRecurrentUsers(true);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(1.42, ticket.getPrice());

    }

    @Test
    @DisplayName("Price for car more than 1 hour")
    public void calculateFareCarWithMoreThanOneHourParkingTime() {
        Date inTime = new Date();
        //45 minutes parking time should give 3/4th parking fare
        inTime.setTime(System.currentTimeMillis() - (90 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurrentUsers(false);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(2.25, ticket.getPrice());
    }

    @Test
    @DisplayName("Price for bike more than 1 hour")
    public void calculateFareBikeWithMoreThanOneHourParkingTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (90 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurrentUsers(false);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(1.50, ticket.getPrice());

    }

}

