/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.csa_smartcampus.mapper;

// imports
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;
import com.mycompany.csa_smartcampus.exception.RoomNotEmptyException;

/**
 *
 * @author ADMIN
 */

@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Room Not Empty");
        error.put("message", ex.getMessage());
        return Response.status(Response.Status.CONFLICT)
                       .entity(error)
                       .type(MediaType.APPLICATION_JSON)
                       .build();
    }
}