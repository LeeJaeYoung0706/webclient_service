package newws.webclient_service.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import newws.webclient_service.service.AuthorizationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RouterFunctionConfig {

    private final AuthorizationService authorizationService;

    @Bean
    public RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route()
                .GET("/auth/**", this::authServiceRoute)
                .GET("/member/**", this::memberServiceRoute)
                .build();
    }

    public Mono<ServerResponse> authServiceRoute(ServerRequest request) {
        String path = request.path();
        HttpMethod method = request.method();
        MediaType contentType = request.headers().contentType().orElse(MediaType.APPLICATION_JSON);
        return authorizationService.authorizationService(path, method, contentType);
    }

    public Mono<ServerResponse> memberServiceRoute(ServerRequest request) {
        String path = request.path();
        return ServerResponse.ok().bodyValue("Routing to Service 2");
    }
}
