# reservation-ms
Project shows microservices architecture with the Spring Cloud stack. The idea behind is to concentrate on microservices infrastructure rather then business logic. Simple reservation system used as a use case for the surrounding system.

### Tech stack
- Spring HATEOAS
- Spring Boot
- Spring Actuator
- Spring Cloud Stream (RabbitMQ as broker)
- Spring Cloud Config Server
- Spring Cloud Netflix
  - Service Discovery (Eureka)
  - Circuit Breaker (Hystrix)
  - Client Side Load Balancing (Ribbon)
  - Intelligent Routing (Zuul)
- Spring Cloud Sleuth

### Architecture overivew
* Spring Boot - as services engine
* Spring Cloud Config Server - externalized configuraiton for services and tools
* Service Registry and Discovery - Netflix Eureka-based service registry
* API Geteway - Netflix Zuul
* Services monitoring - Spring actuator
* Circuit Breaker - Hystrix Circuit Breaker, Hystrix dashboard
* Distributed Tracing - using Spring Cloud Sleuth with Zipkin

### Services description
- config-server
- eureka-server
- hystrix-dashboard
- reservation-service
- reservation-client

### Build instructions
```
git clone https://github.com/Rolan2772/reservation-ms.git
cd reservation-ms
mvn clean install -DskipTests
```

### Run instructions (UNIX)
```
gnome-terminal -e 'sh -c "java -jar config-server/target/config-server-0.0.1-SNAPSHOT.jar; exec bash"'
gnome-terminal -e 'sh -c "java -jar eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar; exec bash"'
docker run -d --hostname my-rabbit --name some-rabbit -p 15672:15672  -p 5672:5672 rabbitmq:3-management
gnome-terminal -e 'sh -c "java -jar reservation-service/target/reservation-service-0.0.1-SNAPSHOT.jar; exec bash"'
gnome-terminal -e 'sh -c "java -jar reservation-client/target/reservation-client-0.0.1NAPSHOT.jar; exec bash"'
docker run -d -p 9411:9411 openzipkin/zipkin
gnome-terminal -e 'sh -c "java -jar hystrix-dashboard/target/hystrix-dashboard-0.0.1-SNAPSHOT.jar; exec bash"'
```
### Config Server endpoints
* Config Server environment endpoint - http://localhost:8888/actuator/env
* Eureka Server configuraiton - http://localhost:8888/eureka-server/default
* Reservation Servise configuration - http://localhost:8888/reservation-service/default
* Reservation Client configuration - http://localhost:8888/reservation-client/default
* Hystrix Dashboard configuration - http://localhost:8888/hystrix-dashboard/default

### Eureka Server endpoints
* Eureka Server dashboard - http://localhost:8761/

### Hystrix Dashboard endpoints
* Hystrix Dashboard - http://localhost:8010/hystrix
* Reservation Client monitoring - http://localhost:8010/hystrix/monitor?stream=http%3A%2F%2Flocalhost%3A9999%2Factuator%2Fhystrix.stream

### Zipkin endpoints
* Zipkin server - http://localhost:9411/zipkin/

### Rabbit endpoints
* RabbitMQ management interface - http://localhost:15672/


