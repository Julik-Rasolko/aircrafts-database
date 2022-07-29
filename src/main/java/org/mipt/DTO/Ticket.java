package org.mipt.DTO;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;

public final class Ticket {
    private String ticketNo;
    private String bookRef;
    private String passengerId;
    private String passengerName;
    private HashMap<String, String> contactData;

    public Ticket() { }

    public Ticket(String data) {
        Pattern pattern = Pattern.compile("(\\w+),(\\w+),(.+),(.+),\"(.*)\"");
        Matcher matcher = pattern.matcher(data);
        if (matcher.matches()) {
            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, String>>() { } .getType();
            ticketNo = matcher.group(1);
            bookRef = matcher.group(2);
            passengerId = matcher.group(3);
            passengerName = matcher.group(4);
            contactData = gson.fromJson(matcher.group(5).replace("\"\"", "\""), type);
        }
    }

    public Ticket(String ticketNo, String bookRef, String passengerId, String passengerName,
                  HashMap<String, String> contactData) {
        ticketNo = ticketNo;
        bookRef = bookRef;
        passengerId = passengerId;
        passengerName = passengerName;
        contactData = contactData;
    }

    public String getTicketNo() {
        return ticketNo;
    }
    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }
    public String getBookRef() {
        return bookRef;
    }
    public void setBookRef(String bookRef) {
        this.bookRef = bookRef;
    }
    public String getPassengerId() {
        return passengerId;
    }
    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }
    public String getPassengerName() {
        return passengerName;
    }
    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }
    public HashMap<String, String> getContactData() {
        return contactData;
    }
    public void setContactData(HashMap<String, String> contactData) {
        this.contactData = contactData;
    }
}
