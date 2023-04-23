package realsoft.carservice.cargateway;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Primary //Override Original swagger implementation with custom swagger resource provider.
public class DocumentationConfig implements SwaggerResourcesProvider {

    @Override
    public List get() {
        List<SwaggerResource> resources = new ArrayList();
        resources.add(swaggerResource("car-service", "/cars/swagger", "2.0"));
        resources.add(swaggerResource("serviceHistory-service", "/swagger-ui/", "2.0"));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }


}