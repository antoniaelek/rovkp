/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework02.task01;

/**
 *
 * @author aelek
 */
public class DebsRecordParser {

    private String medallion;
    private int tripTimeInSecs;

    public void parse(String record) throws Exception {
        String[] splitted = record.split(",");
        if (splitted.length < 9) throw new Exception("Unable to parse record: " + record);
             
        medallion = splitted[0];
        tripTimeInSecs = Integer.parseInt(splitted[8]);
    }

    public String getMedallion() {
        return medallion;
    }

    public int getTripTimeInSecs() {
        return tripTimeInSecs;
    }

}