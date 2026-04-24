# Smart Campus – Sensor & Room Management API

**Module:** 5COSC022W – Client-Server Architectures (2025/26)  
**University of Westminster – School of Computer Science and Engineering**

---

## Table of Contents

1. [API Design Overview](#1-api-design-overview)
2. [Technology Stack](#2-technology-stack)
3. [Project Structure](#3-project-structure)
4. [How to Build \& Run the Project](#4-how-to-build--run-the-project)
5. [Sample curl Commands](#5-sample-curl-commands)
6. [Conceptual Report – Answers to Coursework Questions](#6-conceptual-report--answers-to-coursework-questions)

   * [Part 1 – Service Architecture \& Setup](#part-1--service-architecture--setup)
   * [Part 2 – Room Management](#part-2--room-management)
   * [Part 3 – Sensor Operations \& Linking](#part-3--sensor-operations--linking)
   * [Part 4 – Deep Nesting with Sub-Resources](#part-4--deep-nesting-with-sub-resources)
   * [Part 5 – Advanced Error Handling, Exception Mapping \& Logging](#part-5--advanced-error-handling-exception-mapping--logging)

---

## 1. API Design Overview

The **Smart Campus API** is a fully RESTful web service designed to manage campus infrastructure — specifically **Rooms** and the **Sensors** deployed within them, along with a log of **Sensor Readings**.

### Resource Hierarchy / API endpoints

```
/api/v1                              → Discovery endpoint (API metadata)
/api/v1/rooms                        → Room collection
/api/v1/rooms/{roomId}               → Individual room
/api/v1/sensors                      → Sensor collection (supports ?type= filtering)
/api/v1/sensors/{sensorId}           → Individual sensor
/api/v1/sensors/{sensorId}/readings  → Sensor reading sub-resource (history log)
```

### Key Design Principles

* **Easy to read URI names** – URLs have named after "things" (like /sensors), and not "actions" (like /get-sensor-data).
  
* **Proper HTTP verbs** – `GET` for retrieval, `POST` for creation, `DELETE` for removal.
  
* **Meaningful HTTP status codes** – `201 Created`, `200 OK`, `404 Not Found`, `409 Conflict`, `422 Unprocessable Entity`, `403 Forbidden`, `500 Internal Server Error`.
  
* **Common Language**: Everything is sent and received in JSON format, which is easy for both humans and computers to read.
  
* **In-memory data store** – All data stored in the temporary file `DataStore.java` using HashMaps and ArrayLists.
  
* **Leak-proof error handling** – Custom `ExceptionMapper` classes intercept all known and unknown exceptions, ensuring raw Java stack traces are never exposed to API consumers.

### Data Models (Room, Sensor, SensorReading)

|Model|Key Fields|
|-|-|
|`Room`|`id`, `name`, `capacity`, `sensorIds`|
|`Sensor`|`id`, `type`, `status` (ACTIVE / MAINTENANCE / OFFLINE), `currentValue`, `roomId`|
|`SensorReading`|`id` (UUID), `timestamp`, `value`|

---

## 2. Technology Stack

|Component|Technology|
|-|-|
|Language|Java (JDK 25)|
|Framework|JAX-RS via Jersey (Javax RESTful Web Services)|
|Server|Apache Tomcat 9.0 / GlassFish|
|Build Tool|Maven|
|IDE|NetBeans|
|API Testing|Postman|
|Version Control|GitHub|

>[!NOTE]
>No Spring Boot, no external database, no SQL. All data is stored in in-memory data structures as required by the coursework specification.

---

## 3. Project Structure

```
com.mycompany.csa_smartcampus
│
├── SmartCampusApplication.java        # @ApplicationPath("/api/v1") entry point
├── LoggingFilter.java                 # ContainerRequestFilter + ContainerResponseFilter
├── DataStore.java                     # Shared in-memory data store
│
├── models/                            # Stores basic data about each models (Constructors, getters and setters)
│   ├── Room.java
│   ├── Sensor.java
│   └── SensorReading.java
│
├── resource/
│   ├── DiscoveryResource.java         # GET /api/v1
│   ├── RoomResource.java              # GET|POST /rooms, GET|DELETE /rooms/{id}
│   ├── SensorResource.java            # GET|POST /sensors, sub-resource locator
│   └── SensorReadingResource.java     # GET|POST /sensors/{id}/readings
│
├── service/
│   ├── RoomService.java
│   ├── SensorService.java
│   └── SensorReadingService.java
│
├── exception/
│   ├── RoomNotEmptyException.java
│   ├── LinkedResourceNotFoundException.java
│   └── SensorUnavailableException.java
│
└── mapper/
    ├── RoomNotEmptyExceptionMapper.java          # 409 Conflict
    ├── LinkedResourceNotFoundExceptionMapper.java # 422 Unprocessable Entity
    ├── SensorUnavailableExceptionMapper.java      # 403 Forbidden
    └── GlobalExceptionMapper.java                 # 500 catch-all
```

---

## 4. How to Build & Run the Project

### Step 1 – Clone the Repository

```bash
git clone https://github.com/SarikaImandi/campus-sensor-management-api.git
cd csa-smartcampus
```

### Step 2 – Build the Project in NetBeans

* Open the file in NetBeans, and then right click on the main project folder.
* Then click 'Clean and Build' option.
* Then wait until the bottom loading bar completes and downloads all the dependancies thats needed.

### Step 3 – Configuring the TomCat server

* Navigate to 'Tools -> Servers'.
* Then click on 'Add Server...'
* Choose the 'Apache Tomcat or TomEE' and click on next button
* Complete the other steps and finish the setup.


3. The API will be available at:

```
   http://localhost:8081/
   ```
>[!NOTE]
>This has port 8081 because I changed the tomcat/9.0 version to that specific port due to a minor conflict. 

### Step 4 – Verify the Server is Running

Open a browser or run:


[http://localhost:8081/csa_SmartCampus/api/v1/](http://localhost:8081/csa_SmartCampus/api/v1/)


You should receive a JSON discovery response confirming the API is live.

---

## 5. Sample curl Commands

### 1. Discovery – Get API Metadata

```PostMan
GET http://localhost:8081/csa_SmartCampus/api/v1/
```

**Expected response (200 OK):**

```json
{
    "apiVersion": "1.0",
    "description": "Smart Campus Monitoring API",
    "resources": {
        "rooms": "/api/v1/rooms",
        "sensors": "/api/v1/sensors"
    },
    "adminContact": "admin@gmail.com"
}
```

---

### 2. Create a New Room

```PostMan
POST http://localhost:8081/csa_SmartCampus/api/v1/rooms

{
  "id": "LIB-301",
  "name": "Library Quiet Study",
  "capacity": 50
}
```

**Expected response (201 Created):**

```json
{
  "id": "LIB-301",
  "name": "Library Quiet Study",
  "capacity": 50,
  "sensorIds": []
}
```

---

### 3. Register a New Sensor (linked to an existing Room)

```PostMan
POST http://localhost:8081/csa_SmartCampus/api/v1/sensors

{
  "id": "TEMP-001",
  "type": "Temperature",
  "status": "ACTIVE",
  "currentValue": 22.5,
  "roomId": "LIB-301"
}
```

**Expected response (201 Created):**

```json
{
  "id": "TEMP-001",
  "type": "Temperature",
  "status": "ACTIVE",
  "currentValue": 22.5,
  "roomId": "LIB-301"
}
```

---

### 4. Filter Sensors by Type

```PostMan
GET "http://localhost:8081/csa_SmartCampus/api/v1/sensors?type=Temperature" 
```

**Expected response (200 OK):**

```json
[
  {
    "id": "TEMP-001",
    "type": "Temperature",
    "status": "ACTIVE",
    "currentValue": 22.5,
    "roomId": "LIB-301"
  }
]
```

---

### 5. Post a New Sensor Reading (and auto-update currentValue)

```PostMan
POST http://localhost:8081/csa_SmartCampus/api/v1/sensors/TEMP-001/readings

{
  "value": 24.1
}
```

**Expected response (201 Created):**

```json
{
    "id": "91e0ed60-0f40-4308-a64e-42de90c642f0",
    "timeStamp": 1777008460410,
    "value": 24.1
}
```

The parent sensor's `currentValue` is also updated to `24.1`.

---

### 6. Attempt to Delete a Room That Still Has Sensors (409 Conflict)

```PostMan
DELETE http://localhost:8081/csa_SmartCampus/api/v1/rooms/LIB-301
```

**Expected response (409 Conflict):**

```json
{
    "error": "Room Not Empty",
    "message": "Cannot delete room 'LIB-301': it still has 1 sensor(s) assigned. Please remove all sensors first."
}
```

---

## 6. Conceptual Report – Answers to Coursework Questions

---

### Part 1 – Service Architecture \& Setup

#### Q1.1 – JAX-RS Resource Class Lifecycle: Per-Request vs Singleton

By default, JAX-RS creates a **new instance of each Resource class for every incoming HTTP request** (per-request scope). This is the default lifecycle mandated by the JAX-RS specification and is the behaviour used in this project.

This design decision has a direct and important implication for state management: because each request gets a fresh resource object, **you cannot store application data as instance variables inside a Resource class** — any data written during one request would be lost by the next. To work around this, all shared application data in this project is centralised in a **`DataStore` singleton** class that is shared across all requests. The `DataStore` uses `ConcurrentHashMap` and `CopyOnWriteArrayList` as its underlying data structures, both of which are thread-safe collections from `java.util.concurrent`. This prevents race conditions — for example, two simultaneous POST requests trying to add a sensor to the same room will not corrupt the `sensorIds` list because `ConcurrentHashMap` handles concurrent modifications safely without requiring explicit `synchronized` blocks.

In summary: the per-request lifecycle keeps resource classes stateless and simple, while the singleton `DataStore` provides safe, centralised, thread-safe shared state across the entire application.

---

#### Q1.2 – Why is HATEOAS Considered a Hallmark of Advanced RESTful Design?

HATEOAS (Hypermedia As The Engine Of Application State) is the principle that an API response should not just return data — it should also return **links** that tell the client what actions are available next, and where to navigate in the API. This is inspired by how web browsers work: a user visits a homepage and discovers available pages from links embedded in the page itself, rather than from a separate manual.

This approach benefits client developers in several concrete ways:

1. **Discoverability without prior knowledge** – A client that hits `/api/v1` can discover the full resource map (`/api/v1/rooms`, `/api/v1/sensors`) without needing to read external documentation.
2. **Reduced coupling** – If the API team renames or moves a resource URL, the embedded link in the response reflects the change automatically. Clients that follow links rather than hardcoding URLs do not break.
3. **Self-documentation** – Responses guide developers through the API's capabilities, making onboarding faster and reducing the chance of misuse.

Compared to static documentation (e.g., a PDF), HATEOAS keeps the API's navigation layer always in sync with the live implementation. This project implements a simplified version of this at the discovery endpoint, which returns a `resources` map linking to all primary collections.

---

### Part 2 – Room Management

#### Q2.1 – Returning Only IDs vs Full Room Objects in a List Response

When a client requests `GET /api/v1/rooms`, we have a choice between returning a list of **room IDs only**, or returning the **full room objects** (including name, capacity, and sensorIds).

**Returning IDs only:**

* Pros: Very small response payload, low bandwidth usage, fast to serialise.
* Cons: Forces the client to make additional `GET /api/v1/rooms/{id}` requests for every room it wants details about — this is the classic "N+1 request problem", which can be catastrophic at scale (e.g., 1,000 rooms = 1,001 HTTP round trips).

**Returning full objects:**

* Pros: The client gets everything it needs in a single request — no follow-up calls required.
* Cons: Larger payload, especially if rooms have many fields or nested data. Can be wasteful if the client only needs names for a dropdown list.

In this project, `GET /api/v1/rooms` returns **full room objects**. This is the pragmatic choice for a campus management system where a facilities dashboard typically needs to display room names and capacities together, not just IDs. For very large datasets, pagination or sparse fieldsets (e.g., `?fields=id,name`) could be added as future enhancements.

---

#### Q2.2 – Is DELETE Idempotent in This Implementation?

**Yes, the DELETE operation is idempotent in this implementation**, with a deliberate design choice for what happens on repeated calls.

Idempotency means that sending the same request multiple times produces the same server state as sending it once. For `DELETE /api/v1/rooms/{roomId}`:

* **First call:** The room exists, is removed from the `DataStore`, and the server responds with `200 OK`.
* **Second call (same room ID):** The room no longer exists. The server responds with `404 Not Found`.

The **server state** is identical after both calls — the room is gone. This satisfies the definition of idempotency. The response code differs (`200` vs `404`), but idempotency is about state, not response codes. This is consistent with RFC 7231, which confirms that DELETE is idempotent. The `404` on the second call is informative — it tells the client the resource is already absent — rather than indicating a problem. No unintended side effects occur from the repeated call.

---

### Part 3 – Sensor Operations \& Linking

#### Q3.1 – Consequences of Sending the Wrong Content-Type to a @Consumes Endpoint

The `POST /api/v1/sensors` method is annotated with `@Consumes(MediaType.APPLICATION\\\_JSON)`. This annotation tells the JAX-RS runtime that this method **only accepts requests where the `Content-Type` header is `application/json`**.

If a client sends a request with `Content-Type: text/plain` or `Content-Type: application/xml`, the JAX-RS runtime immediately rejects the request **before even invoking the method**. It returns an **HTTP 415 Unsupported Media Type** response. The request body is never read or parsed, and no business logic executes.

This is a clean, declarative contract enforcement mechanism. It offloads content negotiation to the framework rather than requiring manual `if` checks inside every method. The client is clearly informed that it must send JSON, and the developer does not need to write any defensive parsing code to handle unexpected formats.

---

#### Q3.2 – @QueryParam vs Path Parameter for Filtering: Why Query Parameters are Superior

Two possible designs for filtering sensors by type:

* **Query parameter approach:** `GET /api/v1/sensors?type=CO2`
* **Path parameter approach:** `GET /api/v1/sensors/type/CO2`

The query parameter approach is superior for filtering and searching for the following reasons:

1. **Semantic correctness** – The path `/api/v1/sensors` identifies the **sensors collection** as a resource. A query parameter narrows the view of that collection without implying that `type/CO2` is itself a distinct, addressable resource. Using a path segment for a filter value violates REST's resource-naming conventions.
2. **Optional by nature** – Query parameters are inherently optional. `GET /api/v1/sensors` (no parameter) still works and returns all sensors. With a path approach, you would need a separate route for the unfiltered case.
3. **Composability** – Multiple query parameters can be combined easily: `?type=CO2\\\&status=ACTIVE`. Path-based filters become ugly and non-standard when composed: `/sensors/type/CO2/status/ACTIVE`.
4. **Caching and indexing** – Standard HTTP caches and proxies understand query strings as filter modifiers on a resource. Path-based filter variants may be indexed as entirely different resources by intermediaries.
5. **Industry convention** – REST API design guides (including those from Google, GitHub, and Stripe) consistently recommend query parameters for filtering, sorting, and searching collections.

---

### Part 4 – Deep Nesting with Sub-Resources

#### Q4 – Architectural Benefits of the Sub-Resource Locator Pattern

The Sub-Resource Locator pattern works by having a parent resource class (e.g., `SensorResource`) contain a method annotated with `@Path("{sensorId}/readings")` that **returns an instance of another resource class** (`SensorReadingResource`) rather than returning a response directly. The JAX-RS runtime then dispatches the remainder of the request (e.g., `GET` or `POST`) to that returned instance.

This pattern provides several architectural advantages over defining all paths in one monolithic controller:

1. **Single Responsibility Principle** – `SensorResource` handles sensor CRUD. `SensorReadingResource` handles reading history. Neither class grows beyond its own concern.
2. **Maintainability** – Modifying how readings are stored or paginated only requires touching `SensorReadingResource`. There is no risk of accidentally breaking sensor creation logic.
3. **Testability** – Each resource class can be unit-tested independently. You can inject mock sensor data into `SensorReadingResource` without needing the full sensor layer.
4. **Scalability of complexity** – In a real campus system, readings might have their own sub-resources (e.g., alerts, aggregations). The locator pattern allows nesting to grow naturally without creating a "god class".
5. **Contextual injection** – The `sensorId` resolved in `SensorResource` can be passed as a constructor parameter to `SensorReadingResource`, giving it the correct scope automatically.

---

### Part 5 – Advanced Error Handling, Exception Mapping \& Logging

#### Q5.1 –  Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?

When a client POSTs a new Sensor with a `roomId` that doesn't exist, the correct response is **422 Unprocessable Entity**, not **404 Not Found**.
The key distinction is: **404** means the URL itself wasn't found — but `POST /api/v1/sensors` is a perfectly valid endpoint. **422** means the server understood the request and the JSON was valid, but the content of the payload is logically wrong. Returning **404** here would mislead the client into thinking the endpoint doesn't exist, when the real problem is the data inside the request.
This project uses three custom exception mappers, each returning a semantically appropriate status code:

* **422 Unprocessable Entity** — returned by `LinkedResourceNotFoundExceptionMapper` when the `roomId` in a POST sensor request doesn't exist. The payload is well-formed, but references something that isn't there.
* **409 Conflict** — returned by `RoomNotEmptyExceptionMapper` when a DELETE is attempted on a room that still has sensors assigned. The request is valid, but conflicts with the current server state.
* **403 Forbidden** — returned by `SensorUnavailableExceptionMapper` when a reading is posted to a sensor with status `"MAINTENANCE"`. The server understood the request but is refusing it due to the sensor's current state.

All three mappers return structured JSON error bodies rather than plain text or raw stack traces, giving the client a clear, machine-readable explanation of what went wrong.

---

#### Q5.2 – From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?

Exposing raw Java stack traces to external API consumers presents several serious security risks:

1. **Technology fingerprinting** – A stack trace reveals the exact framework (Jersey, JAX-RS), Java version, and internal class names. An attacker can use this to look up known CVEs (Common Vulnerabilities and Exposures) for those specific library versions.
2. **Internal architecture exposure** – Package names and class hierarchies (e.g., `com.mycompany.csa_smartcampus.service.RoomService`) reveal the internal structure of the application, making it easier to craft targeted exploits.
3. **File system paths** – Stack traces sometimes include absolute file paths (e.g., `/home/deploy/tomcat/webapps/...`), which can expose server directory structure and aid in path traversal or file inclusion attacks.
4. **Data leakage** – If an exception is triggered mid-operation, variable values (which may contain partial user data or IDs) can appear in the trace output.
5. **Aiding brute-force and injection attacks** – Knowing exactly which line and method caused a `NullPointerException` or `IndexOutOfBoundsException` helps an attacker refine malicious inputs to trigger predictable failure states.

The `GlobalExceptionMapper` in this project catches all unhandled `Throwable` instances, logs the full stack trace internally (server-side only), and returns a safe, generic `500 Internal Server Error` JSON response to the client — one that contains no internal implementation details.

---

#### Q5.3 – Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single resource method?

Inserting `Logger.info()` statements manually inside every resource method is a form of code duplication and tightly couples a cross-cutting concern (logging) with business logic. JAX-RS filters solve this in a cleaner way by implementing the **Decorator / Interceptor pattern**:

1. **Single point of maintenance** – The `LoggingFilter` class (implementing both `ContainerRequestFilter` and `ContainerResponseFilter`) handles all logging in one place. If the log format needs to change, only one file is edited.
2. **No pollution of business logic** – Resource methods like `createRoom()` or `getSensor()` contain only business-relevant code. They do not need to know anything about logging — this keeps them readable and focused.
3. **Guaranteed coverage** – Manual logging relies on developers remembering to add log statements in every new method. A filter applies automatically to every request and response without any developer action on individual methods.
4. **Consistency** – Every request is logged in exactly the same format. With manual logging, different developers may log different fields in different styles, making log analysis inconsistent.
5. **Separation of Concerns** – Logging is an infrastructure/observability concern, not a business concern. Filters enforce this separation architecturally.

This same principle applies to other cross-cutting concerns like authentication, CORS headers, and rate limiting — all are best implemented as filters rather than duplicated code inside resource methods.

---
