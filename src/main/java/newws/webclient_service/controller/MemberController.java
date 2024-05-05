package newws.webclient_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final WebClient webClient;


    @GetMapping("/example")
    public Mono<ResponseEntity<String>> getExample() {

        log.info("!!! 여기 왔다.");
        return webClient.mutate()
            .baseUrl("http://localhost:4101")
            .build()
            .get()
            .uri("/auth/test")
            .retrieve()
            .toEntity(String.class);
    }

    @GetMapping("/example2")
    public Mono<ResponseEntity<String>> getExample2() {

        log.info("!!! 여기 왔다.");
        return webClient.mutate()
            .baseUrl("http://localhost:4101")
            .build()
            .get()
            .uri("/auth/test")
            .retrieve()
            .toEntity(String.class);
    }


}
