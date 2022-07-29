package org.mipt.DAO;

import org.mipt.DTO.Aircraft;
import org.mipt.DbConnectionFactory;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class AircraftsDAO {
    public Set<Aircraft> getAllAircrafts() throws SQLException {
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM aircrafts");
            Set<Aircraft> aircrafts = new HashSet<>();
            while (rs.next()) {
                Aircraft aircraft = extractAircraftFromResultSet(rs);
                aircrafts.add(aircraft);
            }
            return aircrafts;

        }
    }

    public boolean insertAircraft(Aircraft aircraft) throws SQLException {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() { } .getType();
        try (Connection connection = DbConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO aircrafts VALUES (?, ?, ?)")) {
            ps.setString(1, aircraft.getAircraftCode());
            ps.setString(2, gson.toJson(aircraft.getModel()));
            ps.setInt(3, aircraft.getRange());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private Aircraft extractAircraftFromResultSet(ResultSet rs) throws SQLException {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() { } .getType();
        Aircraft aircraft = new Aircraft();
        aircraft.setAircraftCode(rs.getString("aircraft_code"));
        aircraft.setModel((HashMap<String, String>) gson.fromJson(rs.getString("model"), type));
        aircraft.setRange(rs.getInt("range"));
        return aircraft;
    }
}
