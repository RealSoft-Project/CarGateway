package realsoft.carservice.cargateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Routes {
    @Bean
    public RouteLocator applicationRouteLocator(RouteLocatorBuilder builder) {


        return builder.routes()
                .route("car-service",p -> p.path("/cars/**")
                        .uri("lb://CAR-SERVICE"))
                .route("driver-service",p -> p.path("/drivers/**")
                        .uri("lb://DRIVER-SERVICE"))
                .route("car-log-service",p -> p.path("/carLogs/**")
                        .uri("lb://CAR-LOGS-SERVICE"))
                .build();
    }
}
