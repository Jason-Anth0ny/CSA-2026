package com.smartcampus.application;

import com.smartcampus.filters.LoggingFilter;
import com.smartcampus.mappers.GlobalExceptionMapper;
import com.smartcampus.mappers.LinkedResourceNotFoundMapper;
import com.smartcampus.mappers.RoomNotEmptyMapper;
import com.smartcampus.mappers.SensorUnavailableMapper;
import com.smartcampus.resources.Discovery;
import com.smartcampus.resources.SensorResource;
import com.smartcampus.resources.SensorRoom;
import org.glassfish.jersey.server.ResourceConfig;

public class AppConfig extends ResourceConfig {

    public AppConfig() {
        register(Discovery.class);
        register(SensorRoom.class);
        register(SensorResource.class);
        register(RoomNotEmptyMapper.class);
        register(LinkedResourceNotFoundMapper.class);
        register(SensorUnavailableMapper.class);
        register(GlobalExceptionMapper.class);
        register(LoggingFilter.class);

        System.out.println("Registered Discovery: " + Discovery.class.getName());
        System.out.println("Registered SensorRoom: " + SensorRoom.class.getName());
        System.out.println("Registered SensorRoom: " + SensorResource.class.getName());
    }
}