package newws.webclient_service.config;


import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import newws.webclient_service.lib.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class WebClientConfig {

    private ExchangeStrategies createExchangeStrategies(){
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
            .codecs(clientCodecConfigurer -> {
                clientCodecConfigurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 50);
                clientCodecConfigurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder());
                clientCodecConfigurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder());
            })
            .build();
        addLogging(exchangeStrategies);
        return exchangeStrategies;
    }

    private void addLogging(ExchangeStrategies exchangeStrategies) {
        exchangeStrategies
            .messageWriters().stream()
            .filter(LoggingCodecSupport.class::isInstance)
            .forEach(writer -> ((LoggingCodecSupport)writer).setEnableLoggingRequestDetails(true));
    }

    private ClientHttpConnector createClientHttpConnector() {
        HttpClient httpClient = HttpClient.create()
            .secure(sslContextSpec -> {
                try {
                    sslContextSpec.sslContext(
                        // 모든 인증서를 신뢰하도록 우선 열어둠
                        SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()
                    );
                } catch (SSLException e) {
                    e.printStackTrace();
                }
            })
            .doOnConnected(conn -> conn
                // 소켓 채널 타임 아웃 핸들러 응답 받지 못할 경우 타임 아웃 발생 시키기 위함.
                .addHandlerLast(new ReadTimeoutHandler(180))
                .addHandlerLast(new WriteTimeoutHandler(180))
            );

        return new ReactorClientHttpConnector(httpClient);
    }

    @Bean
    public WebClient webClient() {
        ExchangeStrategies exchangeStrategies = createExchangeStrategies();

        return WebClient
            .builder()
            .clientConnector(
                createClientHttpConnector()
            )
            .exchangeStrategies(exchangeStrategies)
            // 요청 전 실행할 함수
            .filter(ExchangeFilterFunction.ofRequestProcessor(
                clientRequest -> {
                    log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
                    clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.debug("{} : {}", name, value)));
                    return Mono.just(clientRequest);
                }
            ))
            // 응답 받은 후 실행할 함수
            .filter(ExchangeFilterFunction.ofResponseProcessor(
                clientResponse -> {
                    clientResponse.headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> log.debug("{} : {}", name, value)));
                    return Mono.just(clientResponse);
                }
        ))
        .filter((request, next) -> next.exchange(request)
                .doOnError(throwable -> {
                    // 여기서 오류 처리를 수행
                    log.info("An error occurred during WebClient request", throwable);
                }))
            // Chrome 기준 HTTP 설정
            .defaultHeaders(httpHeaders -> {
                httpHeaders.add("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.87 Safari/537.3");
            })
            .build();
    }
}
