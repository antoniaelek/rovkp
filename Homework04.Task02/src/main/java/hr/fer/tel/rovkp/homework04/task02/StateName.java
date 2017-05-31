/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework04.task02;

/**
 *
 * @author aelek
 */
public class StateName {
    private int id;
    private String name;
    private int year;
    private String gender;
    private String state;
    private int count;

    public StateName() {}   
    
    public StateName(String l) {
        Parse(l);
    }
    
    public boolean Parse(String line) {
        String[] fields = line.split(",");
        if (fields.length < 6) return false;
        
        try {
            this.id = Integer.parseInt(fields[0]);
            this.name = fields[1];
            this.year = Integer.parseInt(fields[2]);
            this.gender = fields[3];
            this.state = fields[4];
            this.count = Integer.parseInt(fields[5]);
        } catch (NumberFormatException exc) {
            return false;
        }
        return true;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int Id) {
        this.id = Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int Year) {
        this.year = Year;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String Gender) {
        this.gender = Gender;
    }

    public String getState() {
        return state;
    }

    public void setState(String State) {
        this.state = State;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int Count) {
        this.count = Count;
    }
    
}
