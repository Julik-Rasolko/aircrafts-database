package org.mipt.DTO;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;

public final class Aircraft {
    private String aircraftCode;
    private HashMap<String, String>  model;
    private int range;

    public Aircraft() { }
    public Aircraft(String data) {
        Pattern pattern = Pattern.compile("(\\w\\w\\w),\"(.*)\",(\\d+)");
        Matcher matcher = pattern.matcher(data);
        if (matcher.matches()) {
            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, String>>() { }.getType();
            aircraftCode = matcher.group(1);
            model = gson.fromJson(matcher.group(2).replace("\"\"", "\""), type);
            range = Integer.parseInt(matcher.group(3));
        }
    }

    public String getAircraftCode() {
        return aircraftCode;
    }
    public void setAircraftCode(String aircraftCode) {
        this.aircraftCode = aircraftCode;
    }
    public HashMap<String, String> getModel() {
        return model;
    }
    public void setModel(HashMap<String, String> model) {
        this.model = model;
    }
    public Integer getRange() {
        return range;
    }
    public void setRange(Integer range) {
        this.range = range;
    }
}
