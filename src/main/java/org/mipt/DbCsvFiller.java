package org.mipt;

import org.mipt.DAO.*;
import org.mipt.DTO.*;

import java.sql.SQLException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.IOException;


public class DbCsvFiller {
    public void fillAircrafts(String csv) throws IOException, SQLException {
        AircraftsDAO aircraftsDao = new AircraftsDAO();
        Path csvFile = Paths.get(csv);
        try (BufferedReader br = Files.newBufferedReader(csvFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                aircraftsDao.insertAircraft(new Aircraft(line));
            }
        }

    }
    public void fillAirports(String csv) throws IOException, SQLException {
        AirportsDAO airportsDao = new AirportsDAO();
        Path csvFile = Paths.get(csv);
        try (BufferedReader br = Files.newBufferedReader(csvFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                airportsDao.insertAirport(new Airport(line));
            }
        }
    }
    public void fillBoardingPasses(String csv) throws IOException, SQLException {
        BoardingPassesDAO boardingPassesDao = new BoardingPassesDAO();
        Path csvFile = Paths.get(csv);
        try (BufferedReader br = Files.newBufferedReader(csvFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                boardingPassesDao.insertBoardingPass(new BoardingPass(line));
            }
        }
    }
    public void fillBookings(String csv) throws IOException, SQLException {
        BookingsDAO bookingsDao = new BookingsDAO();
        Path csvFile = Paths.get(csv);
        try (BufferedReader br = Files.newBufferedReader(csvFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                bookingsDao.insertBooking(new Booking(line));
            }
        }
    }
    public void fillFlights(String csv) throws IOException, SQLException {
        FlightsDAO flightsDao = new FlightsDAO();
        Path csvFile = Paths.get(csv);
        try (BufferedReader br = Files.newBufferedReader(csvFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                flightsDao.insertFlight(new Flight(line));
            }
        }
    }
    public void fillSeats(String csv) throws IOException, SQLException {
        SeatsDAO seatsDao = new SeatsDAO();
        Path csvFile = Paths.get(csv);
        try (BufferedReader br = Files.newBufferedReader(csvFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                seatsDao.insertSeat(new Seat(line));
            }
        }
    }
    public void fillTickets(String csv) throws IOException, SQLException {
        TicketsDAO ticketsDao = new TicketsDAO();
        Path csvFile = Paths.get(csv);
        try (BufferedReader br = Files.newBufferedReader(csvFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                ticketsDao.insertTicket(new Ticket(line));
            }
        }
    }
    public void fillTicketFlights(String csv) throws IOException, SQLException {
        TicketFlightsDAO ticketFlightsDao = new TicketFlightsDAO();
        Path csvFile = Paths.get(csv);
        try (BufferedReader br = Files.newBufferedReader(csvFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                ticketFlightsDao.insertTicketFlight(new TicketFlight(line));
            }
        }
    }
}
