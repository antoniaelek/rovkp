package hr.fer.tel.rovkp.lab02;

import java.text.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class DebsRecordParserTest {
    
    private DebsRecordParser parser = new DebsRecordParser();

    public DebsRecordParserTest() {
        try {
            parser.parse("0,1,2013-01-20 23:01:01,3,4,5,41.474937,-74.913585,8,9,10,11,12,13,14,15,16");
        } catch (ParseException ex) {
            System.err.println(ex);
        }
    }
    
    @Test
    public void testGetTotalAmount() {
        double expected = 16;
        double actual = parser.getProfit();
        Assert.assertEquals(expected, actual,0.0000001);
    }
    
    @Test
    public void testGetCellId1(){
        int[] expecteds = new int[2];
        expecteds[0] = expecteds[1] = 1;
        int[] actuals = parser.getCellId(41.474937, -74.913585);
        Assert.assertArrayEquals(expecteds, actuals);
    }
    
    @Test
    public void testGetCellId2(){
        int[] expecteds = new int[2];
        expecteds[0] = expecteds[1] = 151;
        int[] actuals = parser.getCellId(40.127470,-73.117785);
        Assert.assertArrayEquals(expecteds, actuals);
    }
    
    @Test
    public void testIsCoordinateInArea1(){
        boolean expected = true;
        boolean actual = parser.isInArea(41.474937, -74.913585);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testIsCoordinateInArea2(){
        boolean expected = false;
        boolean actual = parser.isInArea(40.127470,-73.117785);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInArea() {
        Assert.assertEquals(false, parser.isInArea());
    }
    
    @Test
    public void testHoursExtract(){
        Assert.assertEquals(23, parser.getHour());
    }
}
