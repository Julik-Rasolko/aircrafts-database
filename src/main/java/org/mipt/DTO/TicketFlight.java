package org.mipt.DTO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TicketFlight {
    private String ticketNo;
    private int flightId;
    private String fareConditions;
    private double amount;

    public TicketFlight() { }

    public TicketFlight(String data) {
        Pattern pattern = Pattern.compile("(\\w+),(\\d+),(\\w+),(\\d+\\.\\d\\d)");
        Matcher matcher = pattern.matcher(data);
        if (matcher.matches()) {
            ticketNo = matcher.group(1);
            flightId = Integer.parseInt(matcher.group(2));
            fareConditions = matcher.group(3);
            amount = Double.parseDouble(matcher.group(4));
        }
    }

    public TicketFlight(String ticketNo, Integer flightId, String fareConditions, double amount) {
        ticketNo = ticketNo;
        flightId = flightId;
        fareConditions = fareConditions;
        amount = amount;
    }

    public String getTicketNo() {
        return ticketNo;
    }
    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }
    public Integer getFlightId() {
        return flightId;
    }
    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }
    public String getFareConditions() {
        return fareConditions;
    }
    public void setFareConditions(String fareConditions) {
        this.fareConditions = fareConditions;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
