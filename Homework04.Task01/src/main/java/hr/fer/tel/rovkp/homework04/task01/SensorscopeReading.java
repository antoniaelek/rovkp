/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework04.task01;

import java.time.LocalDateTime;
import java.util.Comparator;

/**
 *
 * @author aelek
 */
public class SensorscopeReading implements Comparable<SensorscopeReading> {
    private int stationID;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private int timesincetheepoch;
    private int sequenceNumber;
    private double configSamplingTime;
    private double dataSamplingTime;
    private double radioDutyCycle;
    private double radioTransmissionPower;
    private double radioTransmissionFrequency;
    private double primaryBufferVoltage;
    private double secondaryBufferVoltage;
    private double solarPanelCurrent;
    private double globalCurrent;
    private double energySource;

    public boolean Parse(String line) {
        String[] fields = line.split(" ");
        if (fields.length != 19) return false;
        
        try {
            this.stationID = Integer.parseInt(fields[0]);
            this.year = Integer.parseInt(fields[1]);
            this.month = Integer.parseInt(fields[2]);
            this.day = Integer.parseInt(fields[3]);
            this.hour = Integer.parseInt(fields[4]);
            this.minute = Integer.parseInt(fields[5]);
            this.second = Integer.parseInt(fields[6]);
            this.timesincetheepoch = Integer.parseInt(fields[7]);
            this.sequenceNumber = Integer.parseInt(fields[8]);
            this.configSamplingTime = Double.parseDouble(fields[9]);
            this.dataSamplingTime = Double.parseDouble(fields[10]);
            this.radioDutyCycle = Double.parseDouble(fields[11]);
            this.radioTransmissionPower = Double.parseDouble(fields[12]);
            this.radioTransmissionFrequency = Double.parseDouble(fields[13]);
            this.primaryBufferVoltage = Double.parseDouble(fields[14]);
            this.secondaryBufferVoltage = Double.parseDouble(fields[15]);
            this.solarPanelCurrent = Double.parseDouble(fields[16]);
            this.globalCurrent = Double.parseDouble(fields[17]);
            this.energySource = Double.parseDouble(fields[18]);
        } catch (NumberFormatException exc) {
            return false;
        }
        return true;
    }

    public int getStationID() {
        return stationID;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public int getTimesincetheepoch() {
        return timesincetheepoch;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public double getConfigSamplingTime() {
        return configSamplingTime;
    }

    public double getDataSamplingTime() {
        return dataSamplingTime;
    }

    public double getRadioDutyCycle() {
        return radioDutyCycle;
    }

    public double getRadioTransmissionPower() {
        return radioTransmissionPower;
    }

    public double getRadioTransmissionFrequency() {
        return radioTransmissionFrequency;
    }

    public double getPrimaryBufferVoltage() {
        return primaryBufferVoltage;
    }

    public double getSecondaryBufferVoltage() {
        return secondaryBufferVoltage;
    }

    public double getSolarPanelCurrent() {
        return solarPanelCurrent;
    }

    public double getGlobalCurrent() {
        return globalCurrent;
    }

    public double getEnergySource() {
        return energySource;
    }

    public void setStationID(int stationID) {
        this.stationID = stationID;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public void setTimesincetheepoch(int timesincetheepoch) {
        this.timesincetheepoch = timesincetheepoch;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public void setConfigSamplingTime(double configSamplingTime) {
        this.configSamplingTime = configSamplingTime;
    }

    public void setDataSamplingTime(double dataSamplingTime) {
        this.dataSamplingTime = dataSamplingTime;
    }

    public void setRadioDutyCycle(double radioDutyCycle) {
        this.radioDutyCycle = radioDutyCycle;
    }

    public void setRadioTransmissionPower(double radioTransmissionPower) {
        this.radioTransmissionPower = radioTransmissionPower;
    }

    public void setRadioTransmissionFrequency(double radioTransmissionFrequency) {
        this.radioTransmissionFrequency = radioTransmissionFrequency;
    }

    public void setPrimaryBufferVoltage(double primaryBufferVoltage) {
        this.primaryBufferVoltage = primaryBufferVoltage;
    }

    public void setSecondaryBufferVoltage(double secondaryBufferVoltage) {
        this.secondaryBufferVoltage = secondaryBufferVoltage;
    }

    public void setSolarPanelCurrent(double solarPanelCurrent) {
        this.solarPanelCurrent = solarPanelCurrent;
    }

    public void setGlobalCurrent(double globalCurrent) {
        this.globalCurrent = globalCurrent;
    }

    public void setEnergySource(double energySource) {
        this.energySource = energySource;
    }

    public SensorscopeReading() {}   
    
    public SensorscopeReading(String l) {
        Parse(l);
    }

    @Override
    public String toString() {
        return stationID + "," + year + "," + month + "," + day + "," 
                + hour + "," + minute + "," + second + "," 
                + timesincetheepoch + "," + sequenceNumber + "," 
                + configSamplingTime + "," + dataSamplingTime + "," 
                + radioDutyCycle + "," + radioTransmissionPower + "," 
                + radioTransmissionFrequency + "," + primaryBufferVoltage + "," 
                + secondaryBufferVoltage + "," + solarPanelCurrent + "," 
                + globalCurrent + "," + energySource;
    }

    @Override
    public int compareTo(SensorscopeReading o2) {
        LocalDateTime dt1 = LocalDateTime.of(year, month, day, hour, minute, second);
        LocalDateTime dt2 = LocalDateTime.of(o2.year, o2.month, o2.day, o2.hour, o2.minute, o2.second);
        return dt1.compareTo(dt2);
    }
    
    
}
