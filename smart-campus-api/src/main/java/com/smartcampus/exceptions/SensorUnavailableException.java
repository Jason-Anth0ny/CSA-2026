package com.smartcampus.exceptions;

public class SensorUnavailableException extends RuntimeException {
        private final String sensorId;

        public SensorUnavailableException(String sensorId) {
            super("Sensor is currently unavailable");
            this.sensorId = sensorId;
        }

        public String getSensorId() {
            return sensorId;
        }
}
