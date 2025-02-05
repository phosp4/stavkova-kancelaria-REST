# Stavkova Kancelaria REST

This project provides REST API for Stavkova Kancelaria project. The original project (including the database) was created by [Lukyjex](https://github.com/Lukyjex) and [malikstefan77xd](https://github.com/malikstefan77xd). It uses [stavkova-kancelaria-REST](https://github.com/phosp4/stavkova-kancelaria-REST) The project was made for _Application programming in Java_ class (PAZ1c) on Pavol Jozef Šafárik University in Košice.

## Technologies Used

- Java
- Spring Boot
- Maven
- jOOQ

## Endpoints

The API endpoints are as follows:

## API Endpoints

### Sport Events
- **GET** `/sportevents`: Retrieve a list of sport events.
- **GET** `/sportevents/{eventId}`: Retrieve details of a specific sport event.
- **POST** `/sportevents`: Create a new sport event.
- **PUT** `/sportevents`: Update a sport event.
- **PUT** `/sportevents/hide/{eventId}`: Delete (or rather hide) a specific sport event.

### Users
- **GET** `/users`: Retrieve a list of users.
- **GET** `/users/{userId}`: Retrieve details of a specific user.
- **POST** `/users`: Create a new user.
- **PUT** `/users`: Update a user.
- **DELETE** `/users/{userId}`: Delete a specific user.

### Tickets
- **GET** `/tickets`: Retrieve a list of tickets.
- **GET** `/tickets/{ticketId}`: Retrieve details of a specific ticket.
- **POST** `/tickets`: Create a new ticket.
- **PUT** `/tickets`: Update a ticket.
- **DELETE** `/tickets/{ticketId}`: Delete a specific ticket.

### Possible Outcomes
- **GET** `/possibleoutcomes`: Retrieve a list of possible outcomes.

## License
This project is licensed under the MIT License - see the LICENSE file for details.
