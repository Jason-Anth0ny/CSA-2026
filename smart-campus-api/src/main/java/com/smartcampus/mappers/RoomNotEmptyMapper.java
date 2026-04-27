package com.smartcampus.mappers;

import com.smartcampus.exceptions.RoomNotEmptyException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class RoomNotEmptyMapper implements ExceptionMapper<RoomNotEmptyException> {
    @Override
    public Response toResponse(RoomNotEmptyException e){
        return Response.status(409)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of(
                        "error", "Room still has sensors assigned and cannot be deleted",
                        "roomId", e.getRoomId()
                ))
                .build();
    }
}
