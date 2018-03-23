package rolanb.samples.spring.microservices.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.stream.Stream;

@EnableBinding(Sink.class)
@EnableDiscoveryClient
@IntegrationComponentScan
@SpringBootApplication
public class ReservationServiceApplication {

    @Bean
    CommandLineRunner commandLineRunner(ReservationRepository reservationRepository) {
        return args -> {
            Stream.of("Jane", "Max", "Ted", "Jack")
                    .forEach(name -> reservationRepository.save(Reservation.builder()
                            .name(name)
                            .build()));
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(ReservationServiceApplication.class, args);
    }

}

@MessageEndpoint
class ReservationProcessor {

    @Autowired
    private ReservationRepository reservationRepository;

    @ServiceActivator(inputChannel = Sink.INPUT)
    public void acceptReservations(String name) {
        reservationRepository.save(Reservation.builder()
                .name(name)
                .build());
    }
}

@RefreshScope
@RestController
class MessageController {

    @org.springframework.beans.factory.annotation.Value("${message}")
    private String message;

    @GetMapping("/message")
    public String getMessage() {
        return message;
    }
}

@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation, Long> {

}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
class Reservation {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
