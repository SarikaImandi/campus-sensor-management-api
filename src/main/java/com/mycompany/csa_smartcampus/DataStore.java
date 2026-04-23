/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.csa_smartcampus;

// Importing useful libraries/ packages
import com.mycompany.csa_smartcampus.models.Room;
import com.mycompany.csa_smartcampus.models.Sensor;
import com.mycompany.csa_smartcampus.models.SensorReading;

//import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author ADMIN
 */
public class DataStore {
    
    public static final Map<String, Room> rooms = new HashMap<>();
    public static final Map<String, Sensor> sensors = new HashMap<>();
    public static final Map<String, List<SensorReading>> readings = new HashMap<>();
    
}
