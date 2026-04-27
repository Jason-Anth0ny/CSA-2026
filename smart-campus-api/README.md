# Smart Campus API

A JAX-RS RESTful API for managing campus rooms and sensors.
Built with Jersey + Tomcat (embedded server — no external server needed).

---

## How to Run

### Prerequisites
- Java 17+
- Maven 3.8+

### Build the project
```bash
mvn clean install
```

### Start the server
```bash
mvn exec:java
```

Server starts at: `http://localhost:8080/api/v1`

---

## Project Structure

```
src/main/java/com/smartcampus/
├── application/
│   ├── AppConfig.java                    
│   └── Main.java                         
├── data/
│   └── DataStore.java 
├── exception/
│   ├── RoomNotEmptyException.java
│   ├── LinkedResourceNotFoundException.java
│   └── SensorUnavailableException.java  
├── mappers/
│   ├── RoomNotEmptyMapper.java           
│   ├── LinkedResourceNotFoundMapper.java 
│   ├── SensorUnavailableMapper.java      
│   └── GlobalExceptionMapper.java                      
├── models/
│   ├── Room.java
│   ├── Sensor.java
│   └── SensorReading.java      
├── filter/
│   └── LoggingFilter.java                
├── resources/
│   ├── Discovery.java            
│   ├── SensorResource.java                 
│   ├── SensorRoom.java               
│   └── SensorReadingResource.java    
```

---

## API Endpoints

| Method | Endpoint                      | Description |
|--------|-------------------------------|-------------|
| GET | /api/v1/discover              | Discovery — lists all available resources |
| GET | /api/v1/rooms                 | List all rooms |
| POST | /api/v1/rooms                 | Create a new room |
| GET | /api/v1/rooms/{id}            | Get a specific room |
| DELETE | /api/v1/rooms/{id}            | Delete a room (fails if sensors assigned) |
| GET | /api/v1/sensors               | List all sensors (optional ?type= filter) |
| POST | /api/v1/sensors               | Register a new sensor |
| GET | /api/v1/sensors/{id}/readings | Get all readings for a sensor |
| POST | /api/v1/sensors/{id}/readings | Add a new reading |

---
---

## Report — Question Answers

### In your report, explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures (maps/lists) to prevent data loss or race conditions.

Usually when it comes to a resource class, a new instance is instantiated each time a request is made. Since that happens, we cannot store data inside the resource class itself (i.e.: you can't store rooms within the Room class). Therefore we must create a different static location (in this case the Datastore class) where the data can be stored safely without risk of data loss for as long as the server runs.

### Why is the provision of ”Hypermedia” (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?
HATEOAS is considered the hallmark of advanced RESTful design as it guides the user through what they can do using links in the responses itself. This benefits client developers because this way, the client has fewer changes to make if the internal routing change or if it adds new resources. Overall it allows for less brittle code for client developers.

### When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client side processing
Returning a full object is a relatively resource-heavy process. For instance, if we were to return a single room, then we will have to return its id, name, capacity as well as the list of sensors. If we were to return multiple rooms, it would be a lot more data that would need to be shown in the response, and therefore would require relatively more network bandwidth and processing power on the client's side. Since we also have separate GET APIs we can retrieve the full object if and only if needed. Therefore, returning just the IDs is a lot better option that returning full room objects.

###  Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times
Yes, it is idempotent. Idempotency refers to being able to reproduce the same outcome no matter how many times the steps are repeated. In the case of the DELETE request, when it is called once (if the room exists) it will delete the room. If called again, the room will stay deleted and just return a response saying the room does not exist. This will be the same no matter how many times the client sends the same DELETE request to the server. Therefore, the DELETE operation is idempotent.

###  We explicitly use the @Consumes (MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?
When the POST method is annotated with the @Consumes(MediaType.APPLICATION_JSON), it means that the requests will only be routed to the method if the request's Content-Type is JSON. Otherwise, if the client tries to send a type like text/plain or application/xml, it's treated as a mismatch error and 415 error should be returned.

### You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path (e.g., /api/vl/sensors/type/CO2). Why is the query parameter approach generally considered superior for filtering and searching collections?
@QueryParam is generally considered better and is the standard across the board for API based filtration as it allows the base URL to stay the same where the filters are totally optional. It's cleaner than just adding the type as a part of the path itself. Furthermore, it's more scalable as more filtering criteria such as name and others can be added later with minimal change to the main URL.

### Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive controller class?
Implementing a Sub-Resource locator pattern delegates specific logic based on the request that is being routed in. In the context of our application, that means that based on the request URL, we can decide whether the request directly correlates to the Sensors or SensorReadings. Based on that, the method will delegate to the sub resource as needed. This helps to create clean, readable and more easily maintainable code.

###  Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?
404 means that the server has understood the request, and yet was unable to find the requested resource as it doesn't exist. Meanwhile, 422 means the server understood the request, but wasn't able to carry it out and fulfill it due to an issue in the body itself. Therefore, 422 is more semantically accurate than 404 when the issue is a missing reference inside a valid JSON payload. 

### From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?
When you expose an internal Java stack trace to external API consumers, you're basically exposing the inner working of your application to potential attackers. Generally speaking, a stack trace shows what methods are being called, in what time, in what order and also exactly what went wrong and where. Therefore, by exposing that instead of jsut returning an error, you're essentially telling a potential attacker exactly how your system works and how they can break it.

### Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single resource method?
It allows the user to get the same output but with a significantly lesser number of lines of code. Therefore, using JAX-RS filters allows for less code duplication and lesser code complexity overall.