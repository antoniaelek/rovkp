package hr.fer.tel.rovkp.homework04.task03;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class PollutionReading implements Comparable<PollutionReading>, Serializable {
    private int ozone;
    private int particullate_matter;
    private int carbon_monoxide;
    private int sulfure_dioxide;
    private int nitrogen_dioxide;
    private double longitude;
    private double latitude;
    private Date timestamp;
    private final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    public PollutionReading() {}   
    
    public PollutionReading(String l) {
        Parse(l);
    }
    
    public boolean Parse(String line) {
        String[] fields = line.split(",");
        if (fields.length < 8) return false;
                
        try {
            this.ozone = Integer.parseInt(fields[0]);
            this.particullate_matter = Integer.parseInt(fields[1]);
            this.carbon_monoxide = Integer.parseInt(fields[2]);
            this.sulfure_dioxide = Integer.parseInt(fields[3]);
            this.nitrogen_dioxide = Integer.parseInt(fields[4]);
            this.longitude = Double.parseDouble(fields[5]);
            this.latitude = Double.parseDouble(fields[6]);
            SimpleDateFormat format = new SimpleDateFormat (FORMAT); 
            this.timestamp = format.parse(fields[7]);
        } catch (NumberFormatException | ParseException exc) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return ozone + "," + particullate_matter + "," + carbon_monoxide 
                + "," + sulfure_dioxide + "," + nitrogen_dioxide 
                + "," + longitude + "," + latitude + "," 
                + new SimpleDateFormat(FORMAT, Locale.US).format(timestamp);
    }

    @Override
    public int compareTo(PollutionReading o2) {
        return timestamp.compareTo(o2.timestamp);
    }
    
    public int getOzone() {
        return ozone;
    }

    public int getParticullate_matter() {
        return particullate_matter;
    }

    public int getCarbon_monoxide() {
        return carbon_monoxide;
    }

    public int getSulfure_dioxide() {
        return sulfure_dioxide;
    }

    public int getNitrogen_dioxide() {
        return nitrogen_dioxide;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public Date getTimestamp() {
        return timestamp;
    }
    
    
}
