package com.smartcampus.mappers;

import com.smartcampus.exceptions.RoomNotEmptyException;
import com.smartcampus.exceptions.SensorUnavailableException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

import java.util.Map;

public class SensorUnavailableMapper implements ExceptionMapper<SensorUnavailableException> {
    @Override
    public Response toResponse(SensorUnavailableException e){
        return Response.status(500)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of(
                        "error", "Sensor is under maintenance and is physically disconnected. Try again later.",
                        "roomId", e.getSensorId()
                ))
                .build();
    }
}
