package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
    private TicketDAO ticketDAO;
    float resultTime;
    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        Long inHour = ticket.getInTime().getTime();
        Long outHour = ticket.getOutTime().getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        Long duration = outHour - inHour;

        //Test sur la durée
        if(duration < ((30 * 60 * 1000))){
            resultTime = 0;
        }else {
            resultTime = ((((float) duration / 1000) / 60) / 60);
        }

        // fonctionnalité reduction à 5%

        if (!ticketDAO.isRegularCustumer(ticket.getVehicleRegNumber())) {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(resultTime * Fare.CAR_RATE_PER_HOUR);
                    System.out.println(resultTime * Fare.CAR_RATE_PER_HOUR);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(resultTime * Fare.BIKE_RATE_PER_HOUR);
                    System.out.println(resultTime * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
            }
        } else {
            System.out.println("------ 5% REDUCE ON OVERALL COST -------");


            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice((resultTime * Fare.CAR_RATE_PER_HOUR) - ((resultTime * Fare.CAR_RATE_PER_HOUR) * 5 / 100));
                    System.out.println(ticket.getPrice());
                    break;
                }
                case BIKE: {
                    ticket.setPrice((resultTime * Fare.BIKE_RATE_PER_HOUR) - ((resultTime * Fare.BIKE_RATE_PER_HOUR) * 5 / 100));
                    System.out.println(ticket.getPrice());
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unkown Parking Type");
            }
        }
    }
}