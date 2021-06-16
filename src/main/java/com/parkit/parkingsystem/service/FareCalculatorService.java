package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author camelia
 * Fare calculator service class is used for calculation parking time and fare to pay
 */
public class FareCalculatorService {
    /**
     * @param ticket
     * The calculateFare method calculates the price of parking time ticket.
     *
     * <p> We used the condition IF : If the ticket out time is not found then a error is printed (IllegalArgumentException)</p>
     *
     * <p>Declaration of the variable in hour and outhour for get the ticket when the user get in the parking and when the users
     * leaves the parking. </p>
     *
     * Long hours is the difference between in and out dates in minutes.
     *
     * The variable duration is convert into hours and we declare a double ratio.
     *
     * <p> We declare a boolean if the user is a recurrent users and we calculate the price to pay for parking time including :
     * free parking fare if duration is less than 30 minutes; 5% discount for recurrent users.</p>
     */
    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();
        long hours = (outHour - inHour)/1000/60;
        double duration = (double)hours/60;

        double ratio;
        boolean reduction = ticket.isRecurrentUsers();
        if(reduction) {
            ratio = 0.95;
        } else {
            ratio = 1;
            System.out.println("Welcome back ! As a recurrent user of our parking lot, you'll benefit from a 5% discount");
        }
        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                double ticketPrice = (duration * Fare.CAR_RATE_PER_HOUR * ratio);
                //function which allows to have two digits after the decimal point for the price
                ticket.setPrice(new BigDecimal(ticketPrice).setScale(2, RoundingMode.HALF_UP).doubleValue());
                break;
            }
            case BIKE: {
                double ticketPrice = (duration * Fare.BIKE_RATE_PER_HOUR * ratio);
                ticket.setPrice(new BigDecimal(ticketPrice).setScale(2, RoundingMode.HALF_UP).doubleValue());
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
        // free ticket before 30 minutes
        if(duration < 0.5) {
            ticket.setPrice(0);
            System.out.println("Free ticket");
        }
    }}