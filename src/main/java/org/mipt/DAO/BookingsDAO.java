package org.mipt.DAO;

import org.mipt.DTO.Booking;
import org.mipt.DbConnectionFactory;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class BookingsDAO {
    public Set<Booking> getAllBookings() throws SQLException {
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM bookings");
            Set<Booking> bookings = new HashSet<>();
            while (rs.next()) {
                Booking booking = extractBookingFromResultSet(rs);
                bookings.add(booking);
            }
            return bookings;
        }
    }

    public boolean insertBooking(Booking booking) throws SQLException {
        try (Connection connection = DbConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO bookings VALUES (?, ?, ?)")) {
            ps.setString(1, booking.getBookRef());
            ps.setString(2, booking.getBookDate().toString());
            ps.setDouble(3, booking.getTotalAmount());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private Booking extractBookingFromResultSet(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setBookRef(rs.getString("book_ref"));
        booking.setBookDate(rs.getTimestamp("book_date"));
        booking.setTotalAmount(rs.getDouble("total_amount"));
        return booking;
    }
}
