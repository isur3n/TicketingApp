# This is simple implementation of Ticketing Service Application

## TicketingApp
It has 2 modules. Currently those are built independenty. However pom file can be added so that 
both can be built, run, tested as a single project.

1. TicketingService - Working
2. TicketingAPI - Not complete (future use)

## TicketingService
This module contains business & data access layer in it. It provides 2 booking algorithms as mentioned below.

### Simple
"Simple" algorithm simply allocates nearest "x" number of seats requested during hold. It will prefer closest to screen first and allocate it while holding a seat. Currently, this algorithm is in use.

### CloseGroup
"CloseGroup" algorithm tries to book tickets close to each other. It means, if booking is for 3 people and there are 3 seats available a little farther but adjacent to each other, it will ignore a seat which is close to screen but isolated. It is currently working however, it needs some additional optimization in the logic to make it more robust.

## TicketingAPI 
This is work in progress considering "TicketingService" can be used as a REST API.

## Assumptions
1. Stage is square/rectangle only (row x col model)
2. Logging is console based. It can be changed to file based in /src/main/resources/log4j.properties file.
3. Certain parameters are read from configuration file /src/main/resources/ticketingService.properties.
4. SeatHold expiry results in cancel operation and held seats returned to availability pool.

Since "TicketingApp" isn't fully functional yet because integration is not completed between API & service component. Hence, we need to run below commands from "ticketingService" root directory.

To Build
```
$ mvn clean install
```

To run JUnit
```
$ mvn test
```

To execute 
```
$ java -jar target\ticketingService-1.0.jar
```

## Softwares used
* JDK 1.7
* Maven 3.2.5
