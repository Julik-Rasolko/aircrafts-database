package org.mipt.DAO;

import org.mipt.DTO.Airport;
import org.mipt.DbConnectionFactory;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class AirportsDAO {
    public Set<Airport> getAllAirports() throws SQLException {
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM airports");
            Set<Airport> airports = new HashSet<>();
            while (rs.next()) {
                Airport airport = extractAirportFromResultSet(rs);
                airports.add(airport);
            }
            return airports;

        }
    }

    public boolean insertAirport(Airport airport) throws SQLException {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() { } .getType();
        try (Connection connection = DbConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO airports VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, airport.getAirportCode());
            ps.setString(2, gson.toJson(airport.getAirportName()));
            ps.setString(3, gson.toJson(airport.getCity()));
            ps.setString(4, airport.getCoordinates());
            ps.setString(5, airport.getTimezone());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private Airport extractAirportFromResultSet(ResultSet rs) throws SQLException {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() { } .getType();
        Airport airport = new Airport();
        airport.setAirportCode(rs.getString("airport_code"));
        airport.setAirportName((HashMap<String, String>) gson.fromJson(rs.getString("airport_name"), type));
        airport.setCity((HashMap<String, String>) gson.fromJson(rs.getString("city"), type));
        airport.setCoordinates(rs.getString("coordinates"));
        airport.setTimezone(rs.getString("timezone"));
        return airport;
    }
}
