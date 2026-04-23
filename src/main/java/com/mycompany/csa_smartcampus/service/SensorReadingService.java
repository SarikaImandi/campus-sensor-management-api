/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.csa_smartcampus.service;

//imports
import com.mycompany.csa_smartcampus.exception.SensorUnavailableException;
import com.mycompany.csa_smartcampus.models.Sensor;
import com.mycompany.csa_smartcampus.models.SensorReading;
import com.mycompany.csa_smartcampus.DataStore;

import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author ADMIN
 */

public class SensorReadingService {

    public List<SensorReading> getReadings(String sensorId) {
        return DataStore.readings.getOrDefault(sensorId, new ArrayList<>());
    }

    public SensorReading addReading(String sensorId, SensorReading reading) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            throw new NotFoundException("Sensor not found: " + sensorId);
        }
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(
                "Sensor '" + sensorId + "' is currently under maintenance. " +
                "Readings cannot be recorded at this time.");
        }

        if (reading.getId() == null || reading.getId().isEmpty()) {
            reading.setId(UUID.randomUUID().toString());
        }
        if (reading.getTimeStamp() == 0) {
            reading.setTimeStamp(System.currentTimeMillis());
        }

        DataStore.readings.computeIfAbsent(sensorId, k -> new ArrayList<>()).add(reading);
        sensor.setCurrentValue(reading.getValue());

        return reading;
    }
}
