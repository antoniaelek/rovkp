/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework02.task02;

/**
 *
 * @author aelek
 */
import java.text.ParseException;

public class DebsRecordParser {

    private int passengerCount;
    private double pickupLongitude;
    private double pickupLatitude;
    private double dropoffLongitude;
    private double dropoffLatitude;
    
    private String location;

    public String getLocation() {
        return location;
    }
    
    private int passengerCategory;

    public int getPassengerCategory() {
        return passengerCategory;
    }
    
    private String input;
    
    public String getInput() {
        return input;
    }
        
    public void parse(String record) throws ParseException {
        input = record;
        String[] splitted = record.split(",");
        if (splitted.length < 14) {
            throw new ParseException("Unable to parse record: " + record, record.length());
        }
        
        // drive start and finish coordinates
        pickupLongitude = Double.parseDouble(splitted[10]);
        pickupLatitude = Double.parseDouble(splitted[11]);
        dropoffLongitude = Double.parseDouble(splitted[12]);
        dropoffLatitude = Double.parseDouble(splitted[13]);
        
        // boundaries for the center
        double lowerLong = -74;
        double upperLong = -73.95;
        double lowerLat = 40.75;
        double upperLat = 40.8;
        
        // calculate if drive was in the center        
        if (inRange(pickupLongitude, lowerLong, upperLong) 
                && inRange(pickupLatitude, lowerLat, upperLat)
                && inRange(dropoffLatitude, lowerLat, upperLat) 
                && inRange(dropoffLongitude, lowerLong, upperLong))
            location = "center";
        else location = "not_center";
        
        // number of passengers category
        passengerCount = Integer.parseInt(splitted[7]);
        if (passengerCount == 1)
            passengerCategory = 1;
        else if (passengerCount >= 4)
            passengerCategory = 4;
        else if (passengerCount == 2 || passengerCount == 3)
            passengerCategory = 2;
        else {
            int index = record.substring(0,record.indexOf("," + passengerCount + ",")).length();
            throw new ParseException("Unable to parse record: " + record + ", invalid passenger_count: " + passengerCount, index);
        }
    }
    
    private boolean inRange (double value, double lowerBound, double upperBound){
        return (value >= lowerBound && value <= upperBound);
    }

}
