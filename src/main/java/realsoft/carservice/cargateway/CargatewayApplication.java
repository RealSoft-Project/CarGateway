package realsoft.carservice.cargateway;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition(info =
@Info(title = "CAR LOG API", version = "1.0", description = "Documentation CAR LOG API v1.0")
)
public class CargatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CargatewayApplication.class, args);
    }

}
