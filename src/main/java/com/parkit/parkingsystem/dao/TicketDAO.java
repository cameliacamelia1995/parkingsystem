package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * Ticket DAO class is used for save, get and update the ticket in DB.
 */

public class TicketDAO {

    private static final Logger logger = LogManager.getLogger("TicketDAO");

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();
/**
 *
 * @param ticket
 * This method used to save ticket in DB with the SQL queries.
 * @return boolean.
 */
    public boolean saveTicket(Ticket ticket){
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            //ps.setInt(1,ticket.getId());
            ps.setInt(1,ticket.getParkingSpot().getId());
            ps.setString(2, ticket.getVehicleRegNumber());
            ps.setDouble(3, ticket.getPrice());
            ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
            ps.setTimestamp(5, (ticket.getOutTime() == null)?null: (new Timestamp(ticket.getOutTime().getTime())) );
            ps.execute();
            ps.close();
            return true;
        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
            return false;
        }finally {
            dataBaseConfig.closeConnection(con);
            return false;
        }
    }

    /**
     *
     * @param vehicleRegNumber
     * <p>This method contains the SQL queries to get the ticket information in DB and the vehicle reg number
     * to be recognized in the DB </p>
     * @return ticket
     */
    public Ticket getTicket(String vehicleRegNumber) {
            Connection con = null;
            Ticket ticket = null;
            PreparedStatement ps = null;
            try {
                con = dataBaseConfig.getConnection();
                ps = con.prepareStatement(DBConstants.GET_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            ps.setString(1,vehicleRegNumber);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                ticket = new Ticket();
                ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)),false);
                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(2));
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(rs.getDouble(3));
                ticket.setInTime(rs.getTimestamp(4));
                ticket.setOutTime(rs.getTimestamp(5));
                if (countNumberPlate(vehicleRegNumber)> 1){ // If vehicle reg number is present more than one
                    ticket.setRecurrentUsers(true); // Recurrent User

                }

            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        }finally {
            dataBaseConfig.closeConnection(con);

        }
        return ticket;
    }

    /**
     *
     * @param ticket
     * This method update ticket will add time, price and ID and the ticket will execute SQL queries.
     * @return boolean
     */
    public boolean updateTicket(Ticket ticket) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
            ps.setDouble(1, ticket.getPrice());
            ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
            ps.setInt(3,ticket.getId());
            ps.execute();
            ps.close();
            return true;
        }catch (Exception ex){
            logger.error("Error saving ticket info",ex);
            return false;
        }finally {
            dataBaseConfig.closeConnection(con);
        }
    }

    /**
     *
     * @param vehicleRegNumber
     * This method is used to count the number of vehicle reg number already know in DB.
     * @return count
     */
    public int countNumberPlate(String vehicleRegNumber) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
            try {
                con = dataBaseConfig.getConnection();
                ps = con.prepareStatement(DBConstants.COUNT_NUMBER_PLATE);
                ps.setString(1, vehicleRegNumber); //1 = premier paramètre
                ps.execute();
                rs = ps.executeQuery();
                if (rs.next()) {
                    count = rs.getInt(1); //1 = obtenir le premier champs en int
                }
                dataBaseConfig.closeResultSet(rs);
                dataBaseConfig.closePreparedStatement(ps);
            }catch (Exception ex) {
                logger.error("Error saving ticket info",ex);
            }finally {
                dataBaseConfig.closeConnection(con);
            }
        return count;
    }
        }