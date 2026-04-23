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
import com.mycompany.csa_smartcampus.exception.LinkedResourcesNotFoundException;

/**
 *
 * @author ADMIN
 */

@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourcesNotFoundException> {

    @Override
    public Response toResponse(LinkedResourcesNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Linked Resource Not Found");
        error.put("message", ex.getMessage());
        return Response.status(422) // 422 Unprocessable Entity
                       .entity(error)
                       .type(MediaType.APPLICATION_JSON)
                       .build();
    }
}