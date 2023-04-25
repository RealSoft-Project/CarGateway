package realsoft.carservice.cargateway;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import realsoft.carservice.cargateway.exceptions.CustomErrorModel;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class AuthorizationFilter implements GatewayFilter {
//    private List<String> userNames;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            ServerHttpRequest serverHttpRequest = exchange.getRequest();

            if (!serverHttpRequest.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No Authorization code","0000", HttpStatus.UNAUTHORIZED);
            }

            String auth = serverHttpRequest.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
//            log.info("headers is:",serverHttpRequest.getHeaders().toString());
            String jwt = auth.substring(7);
            if (!isJwtValid(jwt)) {
                return onError(exchange, "Token not valid", "0000", HttpStatus.UNAUTHORIZED);
            }
            if (!isExistRole(jwt)) {
                return onError(exchange, "Not authenticated","0000",  HttpStatus.FORBIDDEN);
            }
            exchange.getResponse().getHeaders().remove(HttpHeaders.ALLOW);
            return chain.filter(
                    exchange);


        } catch (ExpiredJwtException e) {
            final String expiredMsg = e.getMessage();
            log.warn(expiredMsg);
            final String msg = (expiredMsg != null) ? expiredMsg : "Unauthorized";
            return onError(exchange, msg, "0000", HttpStatus.UNAUTHORIZED);
        }
    }

    private Mono<Void> onError(ServerWebExchange serverWebExchange,String message, String code, HttpStatus httpStatus) {
        CustomErrorModel err = new CustomErrorModel(message, code, httpStatus.value());
        ServerHttpResponse serverHttpResponse = serverWebExchange.getResponse();
        ObjectMapper objMapper = new ObjectMapper();
        try{
            byte[] bytes = objMapper.writer().withDefaultPrettyPrinter().writeValueAsBytes(err);
            DataBuffer buffer = serverHttpResponse.bufferFactory().wrap(bytes);
            if(httpStatus== HttpStatus.UNAUTHORIZED) {
                serverHttpResponse.getHeaders().add(HttpHeaders.WWW_AUTHENTICATE,"Bearer realm=\"Not authenticated\"");
            }
            serverHttpResponse.setStatusCode(httpStatus);
            serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return serverHttpResponse.writeWith(Mono.just(buffer));
        } catch(Exception ex){
            var response = serverWebExchange.getResponse();
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return response.setComplete();
        }
    }
    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(jwt);
            String username = decodedJWT.getSubject();
            String[] roles = decodedJWT.getClaim("roles").asArray(String.class); String subject;
//            subject = Jwts.parser().setSigningKey("secret".getBytes())
//                    .parseClaimsJws(jwt)
//                    .getBody()
//                    .toString();

            if (username== null || decodedJWT.getClaims().isEmpty()) {
                returnValue = false;
            }
            return returnValue;
        } catch (MalformedJwtException e) {
            return returnValue = false;
        }
    }

    private boolean isExistRole(String jwt) {
        boolean result = false;
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(jwt);
        String username = decodedJWT.getSubject();
        String[] roles1 = decodedJWT.getClaim("roles").asArray(String.class); String subject;

      Set roles =  Set.of("ROLE_USER", "ROLE_MANAGER","ROLE_ADMIN");
        if (roles.containsAll(List.of(roles1))) {
            result = true;
        }

        return result;
    }
}
