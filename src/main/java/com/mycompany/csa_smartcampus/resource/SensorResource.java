/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.csa_smartcampus.resource;

//imports
import com.mycompany.csa_smartcampus.models.Sensor;
import com.mycompany.csa_smartcampus.service.SensorService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Collection;


/**
 *
 * @author ADMIN
 */

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private final SensorService sensorService = new SensorService();

    @POST
    public Response registerSensor(Sensor sensor) {
        Sensor created = sensorService.createSensor(sensor);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        Collection<Sensor> sensors = sensorService.getAllSensors(type);
        return Response.ok(sensors).build();
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingsResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}
