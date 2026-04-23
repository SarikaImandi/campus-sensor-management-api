/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.csa_smartcampus.resource;

// imports
import com.mycompany.csa_smartcampus.models.SensorReading;
import com.mycompany.csa_smartcampus.service.SensorReadingService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 *
 * @author ADMIN
 */

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String sensorId;
    private final SensorReadingService readingService = new SensorReadingService();

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public Response getReadings() {
        List<SensorReading> readings = readingService.getReadings(sensorId);
        return Response.ok(readings).build();
    }

    @POST
    public Response addReading(SensorReading reading) {
        SensorReading created = readingService.addReading(sensorId, reading);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
}
