package newws.webclient_service.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import newws.webclient_service.lib.Response;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final WebClient webClient;
    private WebClient authWebClient;

    @PostConstruct
    private void settingAuthWebClient() {
        authWebClient = webClient.mutate().baseUrl("http://localhost:4101").build();
    }

    private Mono<ResponseEntity<Object>> createRequest() {
        return null;
    }

    public Mono<ServerResponse> authorizationService(String path, HttpMethod method, MediaType contentType){
        // WebClient를 사용하여 외부 서비스와 통신하는 비동기 작업 수행

        WebClient.RequestHeadersUriSpec<?> get = authWebClient.get();
        WebClient.RequestBodyUriSpec post = authWebClient.post();

        Mono<ResponseEntity<Object>> responseMono = authWebClient
                .get()
                .uri(path)
                .retrieve()
                .toEntity(Object.class);

        // 비동기 작업의 결과를 기다린 후에 결과를 처리하고 클라이언트에게 응답
        return responseMono.flatMap(responseEntity -> {
            // 비동기 작업의 결과를 사용하여 적절한 처리를 수행
            // 예를 들어, ResponseEntity의 내용을 확인하고 그에 따른 응답을 생성할 수 있음
            return ServerResponse.status(responseEntity.getStatusCode())
                    .bodyValue(responseEntity.getBody());
        }).onErrorResume(error -> {
            // 에러 발생 시 적절한 응답을 반환
            return ServerResponse.badRequest().bodyValue("Error occurred: " + error.getMessage());
        });
    }
}
