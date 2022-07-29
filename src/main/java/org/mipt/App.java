package org.mipt;

import java.sql.SQLException;
import java.io.IOException;

public final class App {
    private App() {
    }

    public static void main(String[] args) throws SQLException, IOException  {
        DbCsvFiller bdCsvFiller = new DbCsvFiller();
        bdCsvFiller.fillAircrafts("../data/aircrafts_data.csv");
        bdCsvFiller.fillAirports("../data/airports_data.csv");
        bdCsvFiller.fillBoardingPasses("../data/boarding_passes.csv");
        bdCsvFiller.fillBookings("../data/bookings.csv");
        bdCsvFiller.fillFlights("../data/flights.csv");
        bdCsvFiller.fillSeats("../data/seats.csv");
        bdCsvFiller.fillTickets("../data/tickets.csv");
        bdCsvFiller.fillTicketFlights("../data/ticket_flights.csv");

        DbStatements statements = new DbStatements();
        if ("1".equals(args[0])) {
            statements.getCitiesWithMultipleAirports();
        }
        if ("2".equals(args[0])) {
            statements.getCitiesWithMostCancelledFlights();
        }
        if ("3".equals(args[0])) {
            statements.getShortestRouteByCity();
        }
        if ("4".equals(args[0])) {
            statements.getCancellationsCountByMonth();
        }
//        if ("5".equals(args[0])) {
//            statements.getFlightsFromAndToMscByDay();
//        }
        if ("6".equals(args[0])) {
            statements.cancellAllFlightsOfModel(args[1]);
        }
//        if ("7".equals(args[0])) {
//            statements.cancellFlightsByPeriodAndGetLossByDay(...);
//        }
        if ("8".equals(args[0])) {
            if (!statements.buyTicket(args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9],
                    args[10])) {
                System.out.println("Unsuccessful operation");
            }
        }
    }
}
