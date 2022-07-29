package org.mipt;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Prepares database and provides connection to DB
 */
public final class DbConnectionFactory {
    private static final String PATH_TO_DB = "../sqlite/airtrans.sqlite";
    private static final HikariConfig CONFIG = new HikariConfig();
    private static final DataSource DS;

    static {
        CONFIG.setJdbcUrl("jdbc:sqlite:" + PATH_TO_DB);
        CONFIG.addDataSourceProperty("cachePrepStmts", "true");
        CONFIG.addDataSourceProperty("prepStmtCacheSize", "250");
        CONFIG.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        DS = new HikariDataSource(CONFIG);
        try {
            prepareDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void prepareDB() throws SQLException {
        Connection connection = getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("drop table if exists aircrafts");
            statement.executeUpdate("create table if not exists aircrafts (aircraft_code char(3) not null primary key, "
                    + "model text not null, range int not null)");

            statement.executeUpdate("drop table if exists airports");
            statement.executeUpdate("create table airports (airport_code char(3) not null primary key, "
                    + "airport_name text not null, city text not null, coordinates text not null, "
                    + "timezone text not null)");

            statement.executeUpdate("drop table if exists bookings");
            statement.executeUpdate("create table bookings (book_ref char(6) not null primary key, "
                    + "book_date text not null, total_amount decimal(10, 2) not null)");

            statement.executeUpdate("drop table if exists flights");
            statement.executeUpdate("create table flights (flight_id int not null primary key, "
                    + "flight_no char(6) not null, scheduled_departure text not null, scheduled_arrival text not null, "
                    + "departure_airport char(3) not null, arrival_airport char(3) not null, "
                    + "status varchar(20) not null, aircraft_code char(3) not null, actual_departure text, "
                    + "actual_arrival text, "
                    + "foreign key (aircraft_code) references aircrafts(aircraft_code), "
                    + "foreign key (arrival_airport) references airports(airport_code), "
                    + "foreign key (departure_airport) references airports(airport_code))");

            statement.executeUpdate("drop table if exists seats");
            statement.executeUpdate("create table seats (aircraft_code char(3) not null, seat_no varchar(4) not null, "
                    + "fare_conditions varchar(10) not null, "
                    + "primary key (aircraft_code, seat_no),"
                    + "foreign key (aircraft_code) references aircrafts(aircraft_code))");

            statement.executeUpdate("drop table if exists tickets");
            statement.executeUpdate("create table tickets (ticket_no char(13) not null primary key, "
                    + "book_ref char(6) not null, passenger_id varchar(20) not null, passenger_name text not null, "
                    + "contact_data text, foreign key (book_ref) references bookings(book_ref))");

            statement.executeUpdate("drop table if exists ticket_flights");
            statement.executeUpdate("create table ticket_flights (ticket_no char(13) not null, flight_id int not null,"
                    + " fare_conditions varchar(10) not null, amount decimal(10, 2) not null, "
                    + "primary key (ticket_no, flight_id), "
                    + "foreign key (ticket_no) references tickets(ticket_no), "
                    + "foreign key (flight_id) references flights(flight_id))");

            statement.executeUpdate("drop table if exists boarding_passes");
            statement.executeUpdate("create table boarding_passes (ticket_no char(13) not null, "
                    + "flight_id int not null, boarding_no int not null, seat_no varchar(4) not null,"
                    + "primary key (ticket_no, flight_id), "
                    + "foreign key (ticket_no) references ticket_flights(ticket_no), "
                    + "foreign key (flight_id) references ticket_flights(flight_id))");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DS.getConnection();
    }

    private DbConnectionFactory() { }
}
