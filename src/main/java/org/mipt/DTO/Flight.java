package org.mipt.DTO;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Timestamp;

public final class Flight {
    private int flightId;
    private String flightNo;
    private Timestamp scheduledDeparture;
    private Timestamp scheduledArrival;
    private String departureAirport;
    private String arrivalAirport;
    private String status;
    private String aircraftCode;
    private Timestamp actualDeparture;
    private Timestamp actualArrival;

    public Flight() { }

    public Flight(String data) {
        Pattern pattern = Pattern.compile("(\\d+),(\\w\\w\\w\\w\\w\\w),"
                + "(\\d\\d\\d\\d\\-\\d\\d\\-\\d\\d\\s\\d\\d\\:\\d\\d\\:\\d\\d)\\+\\d\\d,"
                + "(\\d\\d\\d\\d\\-\\d\\d\\-\\d\\d\\s\\d\\d\\:\\d\\d\\:\\d\\d)\\+\\d\\d,"
                + "(\\w\\w\\w),(\\w\\w\\w),(.+),(\\w\\w\\w),(.*),(.*)");
        Matcher matcher = pattern.matcher(data);
        if (matcher.matches()) {
            flightId = Integer.parseInt(matcher.group(1));
            flightNo = matcher.group(2);
            scheduledDeparture = Timestamp.valueOf(matcher.group(3));
            scheduledArrival = Timestamp.valueOf(matcher.group(4));
            departureAirport = matcher.group(5);
            arrivalAirport = matcher.group(6);
            status = matcher.group(7);
            aircraftCode = matcher.group(8);
            if (!matcher.group(9).equals("")) {
                actualDeparture = Timestamp.valueOf(matcher.group(9).split("\\+")[0]);
            }  else {
                actualDeparture = null;
            }
            if (!matcher.group(10).equals("")) {
                actualArrival = Timestamp.valueOf(matcher.group(10).split("\\+")[0]);
            } else {
                actualArrival = null;
            }
        }
    }

    public Integer getFlightId() {
        return flightId;
    }
    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }
    public String getFlightNo() {
        return flightNo;
    }
    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }
    public Timestamp getScheduledDeparture() {
        return scheduledDeparture;
    }
    public void setScheduledDeparture(Timestamp scheduledDeparture) {
        this.scheduledDeparture = scheduledDeparture;
    }
    public Timestamp getScheduledArrival() {
        return scheduledArrival;
    }
    public void setScheduledArrival(Timestamp scheduledArrival) {
        this.scheduledArrival = scheduledArrival;
    }
    public String getDepartureAirport() {
        return departureAirport;
    }
    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }
    public String getArrivalAirport() {
        return arrivalAirport;
    }
    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getAircraftCode() {
        return aircraftCode;
    }
    public void setAircraftCode(String aircraftCode) {
        this.aircraftCode = aircraftCode;
    }
    public Timestamp getActualDeparture() {
        return actualDeparture;
    }
    public void setActualDeparture(Timestamp actualDeparture) {
        this.actualDeparture = actualDeparture;
    }
    public Timestamp getActualArrival() {
        return actualArrival;
    }
}
