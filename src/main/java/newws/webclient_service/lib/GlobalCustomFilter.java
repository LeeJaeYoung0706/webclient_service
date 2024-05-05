package newws.webclient_service.lib;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class GlobalCustomFilter implements WebFilter {

    // 인증이 필요 없는 url 인지 체크
    private class AuthenticationRequestMatcher implements ServerWebExchangeMatcher{

        private final List<String> authenticationList = Arrays.asList("/example");

        @Override
        public Mono<MatchResult> matches(ServerWebExchange exchange) {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().toString();

            if (checkURLPattern(path)) {
                return MatchResult.match();
            }
            return MatchResult.notMatch();
        }

        // list에 요청한 path가 존재 하는 가 체크
        private boolean checkURLPattern(String path) {
            return authenticationList.contains(path);
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        AuthenticationRequestMatcher authenticationRequestMatcher = new AuthenticationRequestMatcher();
        Mono<ServerWebExchangeMatcher.MatchResult> matches = authenticationRequestMatcher.matches(exchange);

        boolean isMatched = matches.block().isMatch();
        if (isMatched) {
            // 요청이 매치되었을 때의 처리
            log.info("Request matched!");
            return chain.filter(exchange);
        } else {
            // 요청이 매치되지 않았을 때의 처리
            log.info("Request ttttt matched!");
            return Mono.empty();
        }
    }
}
