package org.mipt.DTO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BoardingPass {
    private String ticketNo;
    private int flightId;
    private int boardingNo;
    private String seatNo;

    public BoardingPass() { }
    public BoardingPass(String data) {
        Pattern pattern = Pattern.compile("(\\w+),(\\d+),(\\d+),(\\w+)");
        Matcher matcher = pattern.matcher(data);
        if (matcher.matches()) {
            ticketNo = matcher.group(1);
            flightId = Integer.parseInt(matcher.group(2));
            boardingNo = Integer.parseInt(matcher.group(3));
            seatNo = matcher.group(4);
        }
    }

    public BoardingPass(String ticketNo, int flightId, int boardingNo, String seatNo) {
        ticketNo = ticketNo;
        flightId = flightId;
        boardingNo = boardingNo;
        seatNo = seatNo;
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
    public Integer getBoardingNo() {
        return boardingNo;
    }
    public void setBoardingNo(Integer boardingNo) {
        this.boardingNo = boardingNo;
    }
    public String getSeatNo() {
        return seatNo;
    }
    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }
}
