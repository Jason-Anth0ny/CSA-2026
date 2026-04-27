package com.smartcampus.mappers;

import com.smartcampus.exceptions.LinkedResourceNotFoundException;
import com.smartcampus.exceptions.RoomNotEmptyException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class LinkedResourceNotFoundMapper implements ExceptionMapper<LinkedResourceNotFoundException> {
    @Override
    public Response toResponse(LinkedResourceNotFoundException e){
        return Response.status(422)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of(
                        "error", "Room you requested does not exist.Try again with valid room Id",
                        "roomId", e.getRoomId()
                ))
                .build();
    }
}
