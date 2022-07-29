package org.mipt;

import org.mipt.DAO.*;
import org.mipt.DTO.*;


import java.sql.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Locale;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.SQLException;

public class DbStatements {
    public void getCitiesWithMultipleAirports() throws SQLException {
        AirportsDAO airportsDao = new AirportsDAO();

        Set<Airport> airports = airportsDao.getAllAirports();
        HashMap<String, String> airportsByCities = new HashMap<>();
        HashMap<String, Integer> cityAirportsCount = new HashMap<>();
        for (Airport airport : airports) {
            String currCity = airport.getCityRu();
            String currCode = airport.getAirportCode();
            if (airportsByCities.get(currCity) != null) {
                airportsByCities.put(currCity, airportsByCities.get(currCity).concat(", " + currCode));
            } else {
                airportsByCities.put(currCity, currCode);
            }
            if (cityAirportsCount.get(currCity) != null) {
                cityAirportsCount.put(currCity, cityAirportsCount.get(currCity) + 1);
            } else {
                cityAirportsCount.put(currCity, 1);
            }
        }
        for (String city : cityAirportsCount.keySet()) {
            if (cityAirportsCount.get(city) == null || cityAirportsCount.get(city) == 1) {
                airportsByCities.remove(city);
            }
        }
        printStringString(airportsByCities);
    }

    public void getCitiesWithMostCancelledFlights() throws SQLException {
        AirportsDAO airportsDao = new AirportsDAO();
        FlightsDAO flightsDao = new FlightsDAO();

        Map<String, Integer> cancellationNumByCities = flightsDao.getCancellationNumByCities();
        Map<String, Integer> cancellationNumByCitiesDesc = cancellationNumByCities.entrySet().stream()
                .sorted(Collections.reverseOrder(Entry.comparingByValue()))
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue(),
                        (entry1, entry2) -> entry2, LinkedHashMap::new));
        printStringInt(cancellationNumByCitiesDesc);
    }

    public void getShortestRouteByCity() throws SQLException {
        AirportsDAO airportsDao = new AirportsDAO();
        FlightsDAO flightsDao = new FlightsDAO();

        HashMap<Pair<String, String>, Pair<Long, Integer>> routesInfo = flightsDao.getRoutesInfo();
        HashMap<String, Pair<String, Double>> shortestRoutesInfo = new HashMap<>();
        for (Pair<String, String> key : routesInfo.keySet()) {
            Pair<Long, Integer> routeInfo = routesInfo.get(key);
            double mean = (routeInfo.getLeft() * 1.0) / routeInfo.getRight();
            if (shortestRoutesInfo.get(key.getLeft()) == null) {
                shortestRoutesInfo.put(key.getLeft(), Pair.of(key.getRight(), mean));
            } else {
                if (mean < shortestRoutesInfo.get(key.getLeft()).getRight()) {
                    shortestRoutesInfo.put(key.getLeft(), Pair.of(key.getRight(), mean));
                }
            }
        }
        printStringStringDouble(shortestRoutesInfo);
    }

    public void getCancellationsCountByMonth() throws SQLException {
        FlightsDAO flightsDao = new FlightsDAO();

        ArrayList<Long> cancelledDates = flightsDao.getCancelledDates();
        HashMap<String, Integer> cancelledByMonth = new HashMap<>();
        for (long date : cancelledDates) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date);
            String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
            if (cancelledByMonth.get(month) != null) {
                cancelledByMonth.put(month, cancelledByMonth.get(month) + 1);
            } else {
                cancelledByMonth.put(month, 1);
            }
        }
        printStringInt(cancelledByMonth);
    }

    public void cancellAllFlightsOfModel(String model) throws SQLException {
        TicketsDAO ticketsDao = new TicketsDAO();
        TicketFlightsDAO ticketFlightsDao = new TicketFlightsDAO();
        FlightsDAO flightsDao = new FlightsDAO();

        ticketsDao.deleteByModel(model);
        ticketFlightsDao.deleteByModel(model);
        flightsDao.deleteByModel(model);
    }

    public boolean buyTicket(String ticketNo, String bookRef, String passengerId, String passengerName,
                          String contactDataS, String flightIdS, String fareConditions, String amountS,
                          String boardingNoS, String seatNo) throws SQLException {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() { } .getType();
        HashMap<String, String> contactData = gson.fromJson(contactDataS, type);
        int flightId = Integer.parseInt(flightIdS);
        double amount = Double.parseDouble(amountS);
        int boardingNo = Integer.parseInt(boardingNoS);

        FlightsDAO flightsDao = new FlightsDAO();
        SeatsDAO seatsDao = new SeatsDAO();
        TicketsDAO ticketsDao = new TicketsDAO();
        TicketFlightsDAO ticketFlightsDao = new TicketFlightsDAO();
        BoardingPassesDAO boardingPassesDao = new BoardingPassesDAO();

        String aircraftCode = flightsDao.getAircraftCode(flightId);
        if (aircraftCode == null) {
            return false;
        }

        String fareConditionsRes = seatsDao.getAircraftConditions(aircraftCode, seatNo);
        if (!fareConditionsRes.equals(fareConditions)) {
            return false;
        }

        ticketFlightsDao.insertTicketFlight(new TicketFlight(ticketNo, flightId, fareConditions, amount));
        boardingPassesDao.insertBoardingPass(new BoardingPass(ticketNo, flightId, boardingNo, seatNo));
        ticketsDao.insertTicket(new Ticket(ticketNo, bookRef, passengerId, passengerName, contactData));

        return true;
    }

    private void printStringString(Map<String, String> map) {
        for (String key : map.keySet()) {
            System.out.println(String.format("%1$15s: %2$s", key, map.get(key)));
        }
    }

    private void printStringInt(Map<String, Integer> map) {
        for (String key : map.keySet()) {
            System.out.println(String.format("%1$15s: %2$s", key, map.get(key)));
        }
    }

    private void printStringStringDouble(Map<String, Pair<String, Double>> map) {
        for (String key : map.keySet()) {
            System.out.println(String.format("%1$15s: %2$s: %3$s", key, map.get(key).getLeft(),
                    map.get(key).getRight()));
        }
    }
}
