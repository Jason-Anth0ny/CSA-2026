package com.smartcampus.resources;

import com.smartcampus.data.DataStore;
import com.smartcampus.exceptions.SensorUnavailableException;
import com.smartcampus.models.SensorReading;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SensorReadingResource {
    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSensorReading(SensorReading reading){
        System.out.println("Adding sensor reading");
        if (Objects.equals(DataStore.sensors.get(sensorId).getStatus(), "MAINTENANCE")) {
            throw new SensorUnavailableException(sensorId);
        }
        reading.setId(UUID.randomUUID().toString());
        reading.setTimestamp(System.currentTimeMillis());

        DataStore.readings.computeIfAbsent(sensorId, k -> new ArrayList<>()).add(reading);
        DataStore.sensors.get(sensorId).setCurrentValue(reading.getValue());

        return Response.status(201).entity(reading).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensorReadingsHistory(){
        System.out.println("Retrieving sensor readings");
        List<SensorReading> readings = DataStore.readings.get(sensorId);
        return Response.status(200).entity(readings).build();
    }
}
