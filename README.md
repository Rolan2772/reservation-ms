# reservation-ms
Basic example of microservices architecture with Spring Cloud stack. Contains complete example of communication between reservations system microservices including confgiuration server, service discovery, API gateweay, servises monitoring and tracing.

### Used tools
- Spring Boot
- Spring Config Server
- Spring Netflix Eureka
- Spring Netflix Zuul
- Spring Actuator
- Spring Netflix Hystrix
- Spring Netflix Hystrix Dashboard
- Zipkin server

### Build instructions
```
git clone https://github.com/Rolan2772/reservation-ms.git
cd reservation-ms
mvn clean install -DskipTests

### Run instructions
```
gnome-terminal -e 'sh -c "java -jar config-server/target/config-server-0.0.1-SNAPSHOT.jar; exec bash"'
gnome-terminal -e 'sh -c "java -jar eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar; exec bash"'
docker run -d -p 6379:6379 redis
gnome-terminal -e 'sh -c "java -jar reservation-service/target/reservation-service-0.0.1-SNAPSHOT.jar; exec bash"'
gnome-terminal -e 'sh -c "java -jar reservation-client/target/reservation-client-0.0.1NAPSHOT.jar; exec bash"'
docker run -d -p 9411:9411 openzipkin/zipkin
gnome-terminal -e 'sh -c "java -jar hystrix-dashboard/target/hystrix-dashboard-0.0.1-SNAPSHOT.jar; exec bash"'
