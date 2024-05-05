package newws.webclient_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
public class SpringSecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf((csrfConfig) -> csrfConfig.disable())
            .cors( corsConfigurer -> {
                Customizer.withDefaults();
            })
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .authorizeExchange( (authorizeRequests) ->
                authorizeRequests
                    .pathMatchers(HttpMethod.OPTIONS)
                    .permitAll()
                    .anyExchange()
                    .permitAll()
            );
        return http.build();
    }
}
