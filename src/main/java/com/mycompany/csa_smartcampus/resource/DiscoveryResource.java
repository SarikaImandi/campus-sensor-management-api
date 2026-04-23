/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.csa_smartcampus.resource;

// importing essential libraries
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ADMIN
 */

@Path("/")
public class DiscoveryResource {
    
    @GET
    @Produces (MediaType.APPLICATION_JSON)
    
    public Response discover () {
        
        Map<String, Object> info = new HashMap<>();
        info.put("apiVersion", "1.0");
        info.put ("description", "Smart Campus Monitoring API");
        info.put ("adminContact" , "admin@gmail.com");
        
        Map<String, String> links = new HashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");
        info.put("resources", links);
        
        return Response.ok(info).build();
    }
}
