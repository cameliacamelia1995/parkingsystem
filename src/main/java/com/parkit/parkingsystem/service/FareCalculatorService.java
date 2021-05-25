package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();
        double hours = (outHour - inHour)/1000/60; //difference between in and out dates in minutes
        double duration = (double)hours/60; //converts duration into hours
//
        double ratio;
        boolean reduction = ticket.isRecurrentUsers(); //boolean si l'utilisateur est récurent
        if(reduction) {
            ratio = 0.95;
        } else {
            ratio = 1;
        }
        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR * ratio);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR * ratio);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
        if(duration < 0.5) {
            ticket.setPrice(0);
            System.out.println("Ticket gratuit");
        }
    }}
