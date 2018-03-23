package rolanb.samples.spring.microservices.reservationclient;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.Value;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@EnableBinding(Source.class)
@EnableCircuitBreaker
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class ReservationClientApplication {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(ReservationClientApplication.class, args);
    }
}

@Component
class DiscoveryClientExample implements CommandLineRunner {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public void run(String... strings) throws Exception {
        discoveryClient.getInstances("reservation-service").forEach((ServiceInstance s) -> {
            System.out.println(ToStringBuilder.reflectionToString(s));
        });
        discoveryClient.getInstances("reservation-client").forEach((ServiceInstance s) -> {
            System.out.println(ToStringBuilder.reflectionToString(s));
        });
    }
}

@RestController
@RequestMapping("/reservations")
class ReservationApiGateway {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private Source source;

    @PostMapping
    public void writeReservation(@RequestBody String name) {
        Message<String> m = MessageBuilder.withPayload(name).build();
        this.source.output().send(m);
    }

    public Collection<String> fallback() {
        return new ArrayList<>();
    }

    @HystrixCommand(fallbackMethod = "fallback")
    @GetMapping("/names")
    public Collection<String> names() {
        ParameterizedTypeReference<Resources<Reservation>> ptr = new ParameterizedTypeReference<Resources<Reservation>>() {
        };

        ResponseEntity<Resources<Reservation>> responseEntity = restTemplate.exchange("http://reservation-service/reservations",
                HttpMethod.GET,
                null,
                ptr);
        return responseEntity.getBody()
                .getContent()
                .stream()
                .map(Reservation::getName)
                .collect(Collectors.toList());
    }
}

@Value
class Reservation {

    String name;
}