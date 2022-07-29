package org.mipt.DAO;

import org.mipt.DTO.Flight;
import org.mipt.DbConnectionFactory;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.sql.*;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;
import org.apache.commons.lang3.tuple.Pair;

public class FlightsDAO {
    public Set<Flight> getAllFlights() throws SQLException {
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM flights");
            Set<Flight> flights = new HashSet<>();
            while (rs.next()) {
                Flight flight = extractFlightFromResultSet(rs);
                flights.add(flight);
            }
            return flights;
        }
    }

    public boolean insertFlight(Flight flight) throws SQLException {
        try (Connection connection = DbConnectionFactory.getConnection();
             PreparedStatement ps =
                     connection.prepareStatement("INSERT INTO flights VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            ps.setInt(1, flight.getFlightId());
            ps.setString(2, flight.getFlightNo());
            ps.setString(3, flight.getScheduledDeparture().toString());
            ps.setString(4, flight.getScheduledArrival().toString());
            ps.setString(5, flight.getDepartureAirport());
            ps.setString(6, flight.getArrivalAirport());
            ps.setString(7, flight.getStatus());
            ps.setString(8, flight.getAircraftCode());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public HashMap<String, Integer> getCancellationNumByCities() throws SQLException {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() { } .getType();
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT city, count(flight_id) as cancelled_num "
                    + "FROM flights INNER JOIN airports "
                    + "ON flights.departure_airport == airports.airport_code "
                    + "WHERE status = 'Cancelled' "
                    + "GROUP BY city");
            HashMap<String, Integer> cancelledByCity = new HashMap<>();
            while (rs.next()) {
                HashMap<String, String> city = gson.fromJson(rs.getString("city"), type);
                String cityRu = city.get("ru");
                int num = rs.getInt("cancelled_num");
                cancelledByCity.put(cityRu, num);
            }
            return cancelledByCity;

        }
    }

    public HashMap<Pair<String, String>, Pair<Long, Integer>> getRoutesInfo() throws SQLException {
        HashMap<Pair<String, String>, Pair<Long, Integer>> routesInfo = new HashMap<>();
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() { } .getType();
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement stmt = connection.createStatement()) {
            ResultSet rs =
                    stmt.executeQuery("SELECT a1.city, a2.airport_code, scheduled_departure, scheduled_arrival "
                            + "FROM flights "
                            + "INNER JOIN airports a1 "
                            + "ON a1.airport_code == flights.departure_airport "
                            + "INNER JOIN airports a2 "
                            + "ON a2.airport_code == flights.arrival_airport;");
            while (rs.next()) {
                HashMap<String, String> city = gson.fromJson(rs.getString("city"), type);
                String cityRu = city.get("ru");
                String arrivalAirport = rs.getString("airport_code");
                Pair<String, String> key = Pair.of(cityRu, arrivalAirport);
                long duration = Timestamp.valueOf(rs.getString("scheduled_arrival")).getTime()
                        - Timestamp.valueOf(rs.getString("scheduled_departure")).getTime();
                if (routesInfo.get(key) != null) {
                    Pair<Long, Integer> prevInfo = routesInfo.get(key);
                    routesInfo.put(key, Pair.of(prevInfo.getLeft() + duration, prevInfo.getRight() + 1));
                } else {
                    routesInfo.put(key, Pair.of(duration, 1));
                }
            }
            return routesInfo;
        }
    }

    public ArrayList<Long> getCancelledDates() throws SQLException {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() { } .getType();
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT scheduled_departure "
                    + "FROM flights "
                    + "WHERE status = 'Cancelled'");
            ArrayList<Long> cancelledDates = new ArrayList<>();
            while (rs.next()) {
                cancelledDates.add(Timestamp.valueOf(rs.getString("scheduled_departure")).getTime());
            }
            return cancelledDates;

        }
    }

    public void deleteByModel(String model) throws SQLException {
        try (Connection connection = DbConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM flights "
                     + "WHERE aircraft_code IN "
                     + "(SELECT aircraft_code FROM aircrafts WHERE aircraft_code == ?)")) {
            ps.setString(1, model);
            ps.executeUpdate();
        }
    }

    public String getAircraftCode(Integer flightId) throws SQLException {
        try (Connection connection = DbConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT aircraft_code FROM flights "
                     + "WHERE flight_id = ?")) {
            ps.setInt(1, flightId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("aircraft_code");
            }
            return null;
        }
    }

    private Flight extractFlightFromResultSet(ResultSet rs) throws SQLException {
        Flight flight = new Flight();
        flight.setFlightId(rs.getInt("flight_id"));
        flight.setFlightNo(rs.getString("flight_no"));
        flight.setScheduledDeparture(rs.getTimestamp("scheduled_departure"));
        flight.setScheduledArrival(rs.getTimestamp("scheduled_arrival"));
        flight.setDepartureAirport(rs.getString("departure_airport"));
        flight.setArrivalAirport(rs.getString("arrival_airport"));
        flight.setStatus(rs.getString("status"));
        flight.setAircraftCode(rs.getString("aircraft_code"));
        return flight;
    }
}
