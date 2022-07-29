package org.mipt.DTO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Timestamp;

public final class Booking {
    private String bookRef;
    private Timestamp bookDate;
    private double totalAmount;

    public Booking() { }

    public Booking(String data) {
        Pattern pattern = Pattern.compile("(\\w+),(.+)\\+\\d\\d,(\\d+\\.\\d\\d)");
        Matcher matcher = pattern.matcher(data);
        if (matcher.matches()) {
            bookRef = matcher.group(1);
            bookDate = Timestamp.valueOf(matcher.group(2));
            totalAmount = Double.parseDouble(matcher.group(3));
        }
    }

    public String getBookRef() {
        return bookRef;
    }
    public void setBookRef(String bookRef) {
        this.bookRef = bookRef;
    }
    public Timestamp getBookDate() {
        return bookDate;
    }
    public void setBookDate(Timestamp bookDate) {
        this.bookDate = bookDate;
    }
    public Double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
