package com.smartcampus.resources;

import com.smartcampus.data.DataStore;
import com.smartcampus.exceptions.LinkedResourceNotFoundException;
import com.smartcampus.models.Room;
import com.smartcampus.models.Sensor;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("sensors")
public class SensorResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSensor(Sensor sensor) {
        System.out.println("Creating sensor");
        Room room = DataStore.rooms.get(sensor.getRoomId());
        if (room == null) {
            throw new LinkedResourceNotFoundException(sensor.getRoomId());
        }
        DataStore.sensors.put(sensor.getId(), sensor);
        room.setSensorIds(setSensorsToRoom(sensor.getId(), room.getSensorIds()));
        return Response.status(201).entity(sensor).build();
    }

    private List<String> setSensorsToRoom (String sensorId, List<String> sensorIds ) {
        sensorIds.add(sensorId);
        return sensorIds;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response filterAndRetrieveSensor(@QueryParam("type") String type) {
        var result = DataStore.sensors.values().stream()
                .filter(s -> type == null || s.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());

            return Response.status(200).entity(result).build();
    }

    @Path("/{sensorId}")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }

}
