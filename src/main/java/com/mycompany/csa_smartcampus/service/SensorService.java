/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.csa_smartcampus.service;

// imports
import com.mycompany.csa_smartcampus.exception.LinkedResourcesNotFoundException;
import com.mycompany.csa_smartcampus.models.Room;
import com.mycompany.csa_smartcampus.models.Sensor;
import com.mycompany.csa_smartcampus.DataStore;

import javax.ws.rs.NotFoundException;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 *
 * @author ADMIN
 */

public class SensorService {

    public Sensor createSensor(Sensor sensor) {
        Room room = DataStore.rooms.get(sensor.getRoomId());
        if (room == null) {
            throw new LinkedResourcesNotFoundException(
                "Cannot register sensor: room '" + sensor.getRoomId() + "' does not exist.");
        }
        DataStore.sensors.put(sensor.getId(), sensor);
        room.getSensorIds().add(sensor.getId());
        return sensor;
    }

    public Collection<Sensor> getAllSensors(String type) {
        Collection<Sensor> all = DataStore.sensors.values();
        if (type == null || type.isEmpty()) {
            return all;
        }
        return all.stream()
                  .filter(s -> type.equalsIgnoreCase(s.getType()))
                  .collect(Collectors.toList());
    }

    public Sensor getSensor(String sensorId) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            throw new NotFoundException("Sensor not found: " + sensorId);
        }
        return sensor;
    }
}
