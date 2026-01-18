# Hostfully challenge

## Booking app

The proposed solution is a Spring Boot application, using JPA Hibernate to handle database operations, and the database
used is an in-memory H2. Also flyway was used to handle the database schema creation and to insert test data for easy
testing. The project is structured in a Hexagonal architecture fashion.

### Model design

This is the simplified entities model to fit the solution requirements. The model has been designed making emphasis on
the Booking entity.

![model.png](/doc/model.png)

### Running the app locally

To run the application locally, you can directly run the main
class [BookingApplication.java](src/main/java/com/hostfully/bookingapp/BookingApplication.java) in your IDE, or manually
run the command:

```bash
./gradlew bootRun
```

### Test data

After running the app locally, Flyway will populate the database with data in the User and Property entities.

Here are the UUIDs you can use to test the endpoints:

User:

- `ceccd113-a6d3-4b94-b52a-74e2af7654eb`
- `e717bc1f-50e0-404e-92b9-3364570ccc8a`

Property:

- `c3c27fcd-9536-46f9-83ad-5511bf1432fc`
- `d783e385-9e65-4f74-9e2b-ef0a1cc3e807`

Also, there is a Postman collection [here](doc/postman-collection.json) that you can directly import into your Postman
app.

### Endpoints implementation

#### 1. Create a booking

Endpoint:

`POST api/v1/bookings`

Body example:

```json
{
  "propertyId": "c3c27fcd-9536-46f9-83ad-5511bf1432fc",
  "mainGuestId": "ceccd113-a6d3-4b94-b52a-74e2af7654eb",
  "dateFrom": "2026-10-21",
  "dateTo": "2026-10-25",
  "message": "Hi, we will be arriving between 16:00 and 18:00",
  "adults": 2,
  "children": 1
}
```

Request example:

```bash
curl --location 'localhost:8090/api/v1/bookings' \
--header 'Content-Type: application/json' \
--data '{
    "propertyId": "c3c27fcd-9536-46f9-83ad-5511bf1432fc",
    "mainGuestId": "ceccd113-a6d3-4b94-b52a-74e2af7654eb",
    "dateFrom": "2026-10-21",
    "dateTo": "2026-10-25",
    "message": "Hi, we will be arriving between 16:00 and 18:00",
    "adults": 2,
    "children": 1
}'
```

&nbsp;

#### 2. Update booking dates and guest details

Endpoint:

`PUT api/v1/bookings/{booking_id}`

Body example:

```json
{
  "dateFrom": "2026-10-21",
  "dateTo": "2026-10-28",
  "message": "Hi, we will be arriving between 16:00 and 18:00",
  "adults": 2,
  "children": 1
}
```

Request example:

```bash
curl --location --request PUT 'localhost:8090/api/v1/bookings/0084a07b-d5f5-4ae3-bd6f-ab80d1b34b6a' \
--header 'Content-Type: application/json' \
--data '{
    "dateFrom": "2026-10-21",
    "dateTo": "2026-10-28",
    "message": "Hi, we will be arriving between 16:00 and 18:00",
    "adults": 2,
    "children": 1
}'
```

&nbsp;

#### 3. Cancel a booking

Endpoint:

`PATCH api/v1/bookings/{booking_id}/cancel`

Request example:

```bash
curl --location --request PATCH 'localhost:8090/api/v1/bookings/ac7a4e1a-ab04-4791-ab60-0fb657bd173d/cancel'
```

&nbsp;

#### 4. Rebook a canceled booking

Endpoint:

`PATCH api/v1/bookings/{booking_id}/rebook`

Request example:

```bash
curl --location --request PATCH 'localhost:8090/api/v1/bookings/ac7a4e1a-ab04-4791-ab60-0fb657bd173d/rebook'
```

&nbsp;

#### 5. Delete a booking from the system

Endpoint:

`DELETE api/v1/bookings/{booking_id}`

Request example:

```bash
curl --location --request DELETE 'localhost:8090/api/v1/bookings/ac7a4e1a-ab04-4791-ab60-0fb657bd173d'
```

&nbsp;

#### 6. Get a booking

Endpoint:

`GET api/v1/bookings/{booking_id}`

Request example:

```bash
curl --location 'localhost:8090/api/v1/bookings/295ccc59-26e1-4064-a51e-97e5c0004e1c'
```

&nbsp;

#### 7. Create a block

Endpoint:

`POST api/v1/blocks`

Body example:

```json
{
  "propertyId": "c3c27fcd-9536-46f9-83ad-5511bf1432fc",
  "dateFrom": "2026-10-26",
  "dateTo": "2026-10-28"
}
```

Request example:

```bash
curl --location 'localhost:8090/api/v1/blocks' \
--header 'Content-Type: application/json' \
--data '{
    "propertyId": "c3c27fcd-9536-46f9-83ad-5511bf1432fc",
    "dateFrom": "2026-10-26",
    "dateTo": "2026-10-28"
}'
```

&nbsp;

#### 8. Update a block

Endpoint:

`PUT api/v1/blocks/{block_id}`

Body example:

```json
{
  "dateFrom": "2026-10-28",
  "dateTo": "2026-10-30"
}
```

Request example:

```bash
curl --location --request PUT 'localhost:8090/api/v1/blocks/e7e2357c-4de3-4078-9d0e-a27885c52193' \
--header 'Content-Type: application/json' \
--data '{
    "dateFrom": "2026-10-28",
    "dateTo": "2026-10-30"
}'
```

&nbsp;

#### 9. Delete a block

Endpoint:

`DELETE api/v1/blocks/{block_id}`

Request example:

```bash
curl --location --request DELETE 'localhost:8090/api/v1/blocks/e7e2357c-4de3-4078-9d0e-a27885c52193'
```

&nbsp;

#### 10. Get a block

Endpoint:

`GET api/v1/blocks/{block_id}`

Request example:

```bash
curl --location --request DELETE 'localhost:8090/api/v1/blocks/e7e2357c-4de3-4078-9d0e-a27885c52193'
```

&nbsp;

### Accessing H2 console

To access the H2 console, you can go to http://localhost:8090/h2-console

![h2.png](/doc/h2-console.png)

Click `Connect`

I hope you enjoy my solution!