# reservation-ms
Basic architecture overview of microservices with Spring Cloud stack. Contains example of configuration and communication between reservation service and reservation client service including confg server, service registry, API gateweay, monitoring and tracing.

### Used tools and technologies
* Spring Boot - as services engine
* Spring Cloud Config Server - externalized configuraiton for services and tools
* Service Registry and Discovery - Netflix Eureka-based service registry
* API Geteway - Netflix Zuul
* Services monitoring - Spring actuator
* Circuit Breaker - Hystrix Circuit Breaker, Hystrix dashboard
* Distributed Tracing - using Spring Cloud Sleuth with Zipkin

### Build instructions
```
git clone https://github.com/Rolan2772/reservation-ms.git
cd reservation-ms
mvn clean install -DskipTests
```

### Run instructions
```
gnome-terminal -e 'sh -c "java -jar config-server/target/config-server-0.0.1-SNAPSHOT.jar; exec bash"'
gnome-terminal -e 'sh -c "java -jar eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar; exec bash"'
docker run -d -p 6379:6379 redis
gnome-terminal -e 'sh -c "java -jar reservation-service/target/reservation-service-0.0.1-SNAPSHOT.jar; exec bash"'
gnome-terminal -e 'sh -c "java -jar reservation-client/target/reservation-client-0.0.1NAPSHOT.jar; exec bash"'
docker run -d -p 9411:9411 openzipkin/zipkin
gnome-terminal -e 'sh -c "java -jar hystrix-dashboard/target/hystrix-dashboard-0.0.1-SNAPSHOT.jar; exec bash"'
```
