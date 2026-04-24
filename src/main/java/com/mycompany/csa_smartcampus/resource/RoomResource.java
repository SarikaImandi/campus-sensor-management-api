/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.csa_smartcampus.resource;

// imports
import com.mycompany.csa_smartcampus.models.Room;
import com.mycompany.csa_smartcampus.service.RoomService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Collection;

/**
 *
 * @author ADMIN
 */

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private final RoomService roomService = new RoomService();

    @GET
    public Response getAllRooms() {
        Collection<Room> rooms = roomService.getAllRooms();
        return Response.ok(rooms).build();
    }

    @POST
    public Response createRoom(Room room) {
        Room created = roomService.createRoom(room);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = roomService.getRoom(roomId);
        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        roomService.deleteRoom(roomId);
        return Response.ok().build();
    }
}