package newws.webclient_service.lib;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.server.EntityResponse;
import reactor.core.publisher.Mono;

@org.springframework.web.bind.annotation.RestControllerAdvice

public class RestControllerAdvice {

    @ExceptionHandler(RestException.class)
    public Mono<EntityResponse<Response<String>>> example(RestException exception) {
        return EntityResponse.fromObject(Response.builder(ErrorCode.STATE.getValue(), "error").build()).status(HttpStatus.NOT_FOUND).build();
    }

}
