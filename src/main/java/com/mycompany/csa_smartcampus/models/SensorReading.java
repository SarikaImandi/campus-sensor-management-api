/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.csa_smartcampus.models;

/**
 *
 * @author ADMIN
 */
public class SensorReading {
    
    // initializing variables
    private String id;
    private long timeStamp;
    private double value;
    
    public SensorReading () {}
    
    // Constructor
    public SensorReading (String id, long timeStamp, double value) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.value = value;
    }
    
    // Getters
    public String getId () {
        return id;
    }
    
    public long getTimeStamp() {
        return timeStamp;
    }
    
    public double getValue () {
        return value;
    }
    
    
    // Setters
    public void setId(String id) {
        this.id = id;
    }
    
    public void setTimeStamp (long timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    public void setValue (double value) {
        this.value = value;
    }
}
