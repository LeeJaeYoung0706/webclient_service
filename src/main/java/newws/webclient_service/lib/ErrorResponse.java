package newws.webclient_service.lib;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class ErrorResponse {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Mono<Void> error(ServerHttpResponse serverHttpResponse, ErrorCode code) {
        ConcurrentHashMap<String, Integer> returnObject = new ConcurrentHashMap<>();
        returnObject.put("errorCode" , code.getValue());
        try {
            String jsonString = objectMapper.writeValueAsString(returnObject);
            byte[] jsonDataBytes = jsonString.getBytes();
            serverHttpResponse.setStatusCode(HttpStatus.OK);
            serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return serverHttpResponse.writeWith(Mono.just(serverHttpResponse.bufferFactory().wrap(jsonDataBytes)));
        } catch (IOException e) {
            serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
            return serverHttpResponse.setComplete();
        }
    }
}
