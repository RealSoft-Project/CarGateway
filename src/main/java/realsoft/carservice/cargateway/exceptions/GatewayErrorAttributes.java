package realsoft.carservice.cargateway.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class GatewayErrorAttributes extends DefaultErrorAttributes {

  private static final Logger logger = LoggerFactory.getLogger(GatewayErrorAttributes.class);

  @Override
  public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
    Throwable error = super.getError(request);
    Map<String, Object> errorAttributes = new HashMap<>(8);
    errorAttributes.put("message", error.getMessage());

    MergedAnnotation<ResponseStatus> responseStatusAnnotation = MergedAnnotations
        .from(error.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(ResponseStatus.class);

    HttpStatus errorStatus = determineHttpStatus(error, responseStatusAnnotation);

    // Must set, otherwise an error will be reported because the RendereRRRRESPONSE method of DefaultErrorWebexceptionHandler gets this property, re-implementing defaultErrorWebexceptionHandler.
    errorAttributes.put("status", errorStatus.value());
    errorAttributes.put("code", "0000");
    errorAttributes.put("timestamp",  Long.valueOf(new Date().getTime()) );

    return errorAttributes;
  }

  // Copy from DefaultErrorWebexceptionHandler
  private HttpStatus determineHttpStatus(Throwable error, MergedAnnotation<ResponseStatus> responseStatusAnnotation) {
    if (error instanceof ResponseStatusException) {
      return (HttpStatus) ((ResponseStatusException) error).getStatusCode();
    }
    return responseStatusAnnotation.getValue("code", HttpStatus.class).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
  }

}