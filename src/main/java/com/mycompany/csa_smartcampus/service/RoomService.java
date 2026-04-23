/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.csa_smartcampus.service;

// imports
import com.mycompany.csa_smartcampus.exception.RoomNotEmptyException;
import com.mycompany.csa_smartcampus.models.Room;
import com.mycompany.csa_smartcampus.DataStore;

import javax.ws.rs.NotFoundException;
import java.util.Collection;

/**
 *
 * @author ADMIN
 */

public class RoomService {

    public Collection<Room> getAllRooms() {
        return DataStore.rooms.values();
    }

    public Room getRoom(String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            throw new NotFoundException("Room not found: " + roomId);
        }
        return room;
    }

    public Room createRoom(Room room) {
        DataStore.rooms.put(room.getId(), room);
        return room;
    }

    public void deleteRoom(String roomId) {
        Room room = getRoom(roomId);
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(
                "Cannot delete room '" + roomId + "': it still has " +
                room.getSensorIds().size() + " sensor(s) assigned. " +
                "Please remove all sensors first.");
        }
        DataStore.rooms.remove(roomId);
    }
}
