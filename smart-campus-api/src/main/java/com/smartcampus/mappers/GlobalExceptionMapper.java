package com.smartcampus.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

import java.util.Map;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable t) {
        return Response.status(500)
                .entity(Map.of("error", "Internal server error"))
                .type(APPLICATION_JSON).build();
    }
}
