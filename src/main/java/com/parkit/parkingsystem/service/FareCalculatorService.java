package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();
        //difference between in and out dates in minutes
        long hours = (outHour - inHour)/1000/60;
        //converts duration into hours
        double duration = (double)hours/60;
//
        double ratio;
        //boolean si l'utilisateur est récurent
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
//                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR * ratio);
                //fonction qui permet d'avoir deux chiffres après la virgule pour le prix
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
        if(duration < 0.5) {
            ticket.setPrice(0);
            System.out.println("Free ticket");
        }
    }}
