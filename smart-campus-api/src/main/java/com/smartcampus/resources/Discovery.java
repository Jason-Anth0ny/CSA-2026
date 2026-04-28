package com.smartcampus.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

@Path("")
public class Discovery {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response discover() {
        System.out.println("Fetching details");
        Map<String, Object> info = new HashMap<>();
        info.put("version", "1.0");
        info.put("contact", "admin@uni.ac.uk");
        info.put("links", Map.of("rooms", "/api/v1/rooms", "sensors", "/api/v1/sensors"));
        return Response.ok(info).build();
    }

}
