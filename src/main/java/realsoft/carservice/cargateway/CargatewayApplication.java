package realsoft.carservice.cargateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CargatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CargatewayApplication.class, args);
    }

}
