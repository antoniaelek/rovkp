package hr.fer.tel.rovkp.lab02;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DebsRecordParser {

    private final double GRID_HEIGHT = 0.008983112;
    private final double GRID_WIDTH = 0.011972;
    
    private final double BEGIN_LON = -74.913585;
    private final double BEGIN_LAT = 41.474937;
    
    private final int BEGIN_INDEX_WIDTH = 0;
    private final int BEGIN_INDEX_HEIGHT = 0;
    private final int END_INDEX_WIDTH = 151;
    private final int END_INDEX_HEIGHT = 151;
        
    private double pickupLongitude;
    private double pickupLatitude;
    private double dropoffLongitude;
    private double dropoffLatitude;
    
    private double profit;

    public double getProfit() {
        return profit;
    }
    
    private int hour;

    public int getHour() {
        return hour;
    }
   
    public String getCellIdStr() {
        return getCellId()[0] + "." + getCellId()[1];
    }

    public void parse(String record) throws ParseException {
        String[] splitted = record.split(",");
        if (splitted.length < 17) {
            throw new ParseException("Unable to parse record: " + record, record.length());
        }

        // profit
        profit = Double.parseDouble(splitted[16]);
        
        // hour; input format is 2013-01-01 00:00:00
        String dateTimeString = splitted[2];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime = format.parse(dateTimeString); 
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);  
        hour = cal.get(Calendar.HOUR_OF_DAY);
        
        // drive start and finish coordinates
        pickupLongitude = Double.parseDouble(splitted[6]);
        pickupLatitude = Double.parseDouble(splitted[7]);
        dropoffLongitude = Double.parseDouble(splitted[8]);
        dropoffLatitude = Double.parseDouble(splitted[9]);
    }
    
    public boolean isInArea() {
        if (!isInArea(pickupLatitude, pickupLongitude)) return false;
        return isInArea(dropoffLatitude, dropoffLongitude);
    }
    
    public boolean isInArea(double lat, double lon){
        int[] cell = getCellId(lat, lon);
        if (cell[0] < BEGIN_INDEX_WIDTH || cell[0] >= END_INDEX_WIDTH )
            return false;
        return !(cell[1] < BEGIN_INDEX_HEIGHT || cell[1] >= END_INDEX_HEIGHT);
    }
    
    public int[] getCellId() {
        int[] cellId = new int[2];
        cellId[0] = (int) (((pickupLongitude - BEGIN_LON) / GRID_WIDTH) + 1);
        cellId[1] = (int) (((BEGIN_LAT - pickupLatitude) / GRID_HEIGHT) + 1);
        return cellId;
    }
    
    public int[] getCellId(double lat, double lon) {
        int[] cellId = new int[2];
        cellId[0] = (int) (((lon - BEGIN_LON) / GRID_WIDTH) + 1);
        cellId[1] = (int) (((BEGIN_LAT - lat) / GRID_HEIGHT) + 1);
        return cellId;
    }
}
