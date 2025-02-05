# Stavkova Kancelaria REST

This project provides REST API for Stavkova Kancelaria project. The original project (including the database) was created by [Lukyjex](https://github.com/Lukyjex) and [malikstefan77xd](https://github.com/malikstefan77xd). The project was made for _Application programming in Java_ class (PAZ1c) on Pavol Jozef Šafárik University in Košice.

## Technologies Used

- Java
- Spring Boot
- Maven
- jOOQ

## REST API

The project uses a REST API to manage sport events, users, and tickets. The API endpoints are as follows:

- `GET /sportevents`: Retrieve a list of sport events.
- `GET /sportevents/:eventId`: Retrieve details of a specific sport event.
- `PUT /sportevents`: Update a sport event.
- `GET /users`: Retrieve a list of users.
- `GET /users/:userId`: Retrieve details of a specific user.
- `PUT /users`: Update a user.
- `GET /tickets`: Retrieve a list of tickets.
- `GET /tickets/:ticketId`: Retrieve details of a specific ticket.
- `PUT /tickets`: Update a ticket.
