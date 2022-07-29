package org.mipt.DTO;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;

public final class Airport {
    private String airportCode;
    private HashMap<String, String>  airportName;
    private HashMap<String, String>  city;
    private String coordinates;
    private String timezone;

    public Airport() { }
    public Airport(String data) {
        Pattern pattern = Pattern.compile("(\\w\\w\\w),\"(.*)\",\"(.*)\",\"(.*)\",(.*)");
        Matcher matcher = pattern.matcher(data);
        if (matcher.matches()) {
            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, String>>() { } .getType();
            airportCode = matcher.group(1);
            airportName = gson.fromJson(matcher.group(2).replace("\"\"", "\""), type);
            city = gson.fromJson(matcher.group(3).replace("\"\"", "\""), type);
            coordinates = matcher.group(4);
            timezone = matcher.group(5);
        }
    }

    public String getAirportCode() {
        return airportCode;
    }
    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }
    public HashMap<String, String> getAirportName() {
        return airportName;
    }
    public String getAirportNameRu() {
        return airportName.get("ru");
    }
    public String getAirportNameEn() {
        return airportName.get("en");
    }
    public void setAirportName(HashMap<String, String> airportName) {
        this.airportName = airportName;
    }
    public HashMap<String, String> getCity() {
        return city;
    }
    public String getCityRu() {
        return city.get("ru");
    }
    public String getCityEn() {
        return city.get("en");
    }
    public void setCity(HashMap<String, String> city) {
        this.city = city;
    }
    public String getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
    public String getTimezone() {
        return timezone;
    }
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
