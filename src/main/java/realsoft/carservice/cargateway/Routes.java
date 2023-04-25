package realsoft.carservice.cargateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Routes {
    final AuthorizationFilter authorizationHeaderFilter;

    public Routes(AuthorizationFilter authorizationHeaderFilter) {
        this.authorizationHeaderFilter = authorizationHeaderFilter;
    }

    @Bean
    public RouteLocator applicationRouteLocator(RouteLocatorBuilder builder) {


        return builder.routes()
                .route("car-service",p -> p.path("/cars/**")
                        .filters(filter->filter.filter(authorizationHeaderFilter))
                        .uri("lb://CAR-SERVICE"))
                .route("driver-service",p -> p.path("/drivers/**")
                        .filters(filter->filter.filter(authorizationHeaderFilter))
                        .uri("lb://DRIVER-SERVICE"))
                .route("car-log-service",p -> p.path("/carLogs/**")
                        .filters(filter->filter.filter(authorizationHeaderFilter))
                        .uri("lb://CAR-LOGS-SERVICE"))
                .build();
    }
}
