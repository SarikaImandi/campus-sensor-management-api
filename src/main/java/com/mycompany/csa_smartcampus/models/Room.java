/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.csa_smartcampus.models;

// Importing useful packages
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class Room {
    
    // Initializing Variables
    private String id;
    private String name;
    private int capacity;
    private List<String> sensorIds;
    
    public Room() { this.sensorIds = new ArrayList<>(); }
    
    // Constructor
    public Room (String id, String name, int capacity) {
        
        this.id = id; 
        this.name = name;
        this.capacity = capacity;
        this.sensorIds = new ArrayList<>();
        
    }
    
    // Getters
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public int getCapacity () {
        return capacity;
    }
    
    public List<String> getSensorIds () {
        return sensorIds;
    }
    
    
    // Setters
    public void setId (String id) {
        this.id = id;
    }
    
    public void setName (String name) {
        this.name = name;
    }
    
    public void setCapacity (int capacity) {
        this.capacity = capacity;
    }
    
    public void setSensorIds (List<String> sensorIds) {
        this.sensorIds = sensorIds; 
    }
}

