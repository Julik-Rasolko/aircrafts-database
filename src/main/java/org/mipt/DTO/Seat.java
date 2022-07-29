package org.mipt.DTO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Seat {
    private String aircraftCode;
    private String seatNo;
    private String fareConditions;

    public Seat() { }

    public Seat(String data) {
        Pattern pattern = Pattern.compile("(\\w+),(\\w+),(\\w+)");
        Matcher matcher = pattern.matcher(data);
        if (matcher.matches()) {
            aircraftCode = matcher.group(1);
            seatNo = matcher.group(2);
            fareConditions = matcher.group(3);
        }
    }

    public String getAircraftCode() {
        return aircraftCode;
    }
    public void setAircraftCode(String aircraftCode) {
        this.aircraftCode = aircraftCode;
    }
    public String getSeatNo() {
        return seatNo;
    }
    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }
    public String getFareConditions() {
        return fareConditions;
    }
    public void setFareConditions(String fareConditions) {
        this.fareConditions = fareConditions;
    }
}
