package com.smartcampus.resources;

import com.smartcampus.data.DataStore;
import com.smartcampus.exceptions.RoomNotEmptyException;
import com.smartcampus.models.Room;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Collection;
import java.util.Map;

@Path("rooms")
public class SensorRoom {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRoom(Room room) {
        System.out.println("Creating room");
        DataStore.rooms.put(room.getId(), room);
        return Response.status(201).entity(room).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRooms() {
        System.out.println("Fetching rooms");
        Collection<Room> allRooms = DataStore.rooms.values();
        return Response.ok(allRooms).build();
    }

    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomsById(@PathParam("roomId") String roomId) {
        System.out.println("Fetching rooms by Id");
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            return Response.status(404)
                    .entity(Map.of("error", "Room not found", "id", roomId))
                    .build();
        }
        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoomsById(@PathParam("roomId") String roomId) {
        System.out.println("Attempting room deletion");
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            return Response.status(404).build();
        } else if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(roomId);
        }

        DataStore.rooms.remove(roomId);
        return Response.status(200).build();
 }

}
