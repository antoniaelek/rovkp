/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework02.task01;

import java.text.ParseException;

/**
 *
 * @author aelek
 */
public class DebsRecordParser {

    private String medallion;
    private int tripTimeInSecs;

    public void parse(String record) throws ParseException {
        String[] splitted = record.split(",");
        if (splitted.length < 9) {
            throw new ParseException("Unable to parse record: " + record, record.length());
        }
             
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