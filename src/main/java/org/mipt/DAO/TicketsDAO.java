package org.mipt.DAO;

import org.mipt.DTO.Ticket;
import org.mipt.DbConnectionFactory;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class TicketsDAO {
    public Set<Ticket> getAllTickets() throws SQLException {
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tickets");
            Set<Ticket> tickets = new HashSet<>();
            while (rs.next()) {
                Ticket ticket = extractTicketFromResultSet(rs);
                tickets.add(ticket);
            }
            return tickets;

        }
    }

    public boolean insertTicket(Ticket ticket) throws SQLException {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() { } .getType();
        try (Connection connection = DbConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO tickets VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, ticket.getTicketNo());
            ps.setString(2, ticket.getBookRef());
            ps.setString(3, ticket.getPassengerId());
            ps.setString(4, ticket.getPassengerName());
            ps.setString(5, gson.toJson(ticket.getContactData()));
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void deleteByModel(String model) throws SQLException {
        try (Connection connection = DbConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM tickets "
                    + "WHERE ticket_no IN "
                    + "(SELECT ticket_no FROM ticket_flights "
                    + "WHERE flight_id IN "
                    + "(SELECT flight_id FROM flights "
                    + "WHERE aircraft_code IN "
                    + "(SELECT aircraft_code FROM aircrafts WHERE aircraft_code == ?)))")) {
            ps.setString(1, model);
            ps.executeUpdate();
        }
    }

    private Ticket extractTicketFromResultSet(ResultSet rs) throws SQLException {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() { } .getType();
        Ticket ticket = new Ticket();
        ticket.setTicketNo(rs.getString("ticket_no"));
        ticket.setBookRef(rs.getString("book_ref"));
        ticket.setPassengerId(rs.getString("passenger_id"));
        ticket.setPassengerName(rs.getString("passenger_name"));
        ticket.setContactData((HashMap<String, String>) gson.fromJson(rs.getString("contact_data"), type));
        return ticket;
    }
}
