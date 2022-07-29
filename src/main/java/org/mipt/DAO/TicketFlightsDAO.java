package org.mipt.DAO;

import org.mipt.DTO.TicketFlight;
import org.mipt.DbConnectionFactory;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class TicketFlightsDAO {
    public Set<TicketFlight> getAllTicketFlights() throws SQLException {
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM ticket_flights");
            Set<TicketFlight> ticketFlights = new HashSet<>();
            while (rs.next()) {
                TicketFlight ticketFlight = extractTicketFlightFromResultSet(rs);
                ticketFlights.add(ticketFlight);
            }
            return ticketFlights;
        }
    }

    public boolean insertTicketFlight(TicketFlight ticketFlight) throws SQLException {
        try (Connection connection = DbConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO ticket_flights VALUES (?, ?, ?, ?)")) {
            ps.setString(1, ticketFlight.getTicketNo());
            ps.setInt(2, ticketFlight.getFlightId());
            ps.setString(3, ticketFlight.getFareConditions());
            ps.setDouble(4, ticketFlight.getAmount());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void deleteByModel(String model) throws SQLException {
        try (Connection connection = DbConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM ticket_flights "
                     + "WHERE flight_id IN "
                     + "(SELECT flight_id FROM flights "
                     + "WHERE aircraft_code IN "
                     + "(SELECT aircraft_code FROM aircrafts WHERE aircraft_code == ?))")) {
            ps.setString(1, model);
            ps.executeUpdate();
        }
    }

    private TicketFlight extractTicketFlightFromResultSet(ResultSet rs) throws SQLException {
        TicketFlight ticketFlight = new TicketFlight();
        ticketFlight.setTicketNo(rs.getString("ticket_no"));
        ticketFlight.setFlightId(rs.getInt("flight_id"));
        ticketFlight.setFareConditions(rs.getString("fare_conditions"));
        ticketFlight.setAmount(rs.getDouble("amount"));
        return ticketFlight;
    }
}
