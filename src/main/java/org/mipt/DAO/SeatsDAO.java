package org.mipt.DAO;

import org.mipt.DTO.Seat;
import org.mipt.DbConnectionFactory;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class SeatsDAO {
    public Set<Seat> getAllSeats() throws SQLException {
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM seats");
            Set<Seat> seats = new HashSet<>();
            while (rs.next()) {
                Seat seat = extractSeatFromResultSet(rs);
                seats.add(seat);
            }
            return seats;
        }
    }

    public boolean insertSeat(Seat seat) throws SQLException {
        try (Connection connection = DbConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO seats VALUES (?, ?, ?)")) {
            ps.setString(1, seat.getAircraftCode());
            ps.setString(2, seat.getSeatNo());
            ps.setString(3, seat.getFareConditions());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public String getAircraftConditions(String aircraftCode, String seatNo) throws SQLException {
        try (Connection connection = DbConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT fare_conditions FROM seats "
                     + "WHERE aircraft_code = ? AND seat_no = ?")) {
            ps.setString(1, aircraftCode);
            ps.setString(1, seatNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("fare_conditions");
            }
            return null;
        }
    }

    private Seat extractSeatFromResultSet(ResultSet rs) throws SQLException {
        Seat seat = new Seat();
        seat.setAircraftCode(rs.getString("aircraft_code"));
        seat.setSeatNo(rs.getString("seat_no"));
        seat.setFareConditions(rs.getString("fare_conditions"));
        return seat;
    }
}
