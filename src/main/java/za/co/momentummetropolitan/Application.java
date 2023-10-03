package za.co.momentummetropolitan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import za.co.momentummetropolitan.enums.AuthoritiesConst;

@SpringBootApplication
public class Application {
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity security) throws Exception {
        security.authorizeHttpRequests(authz -> {
            authz.requestMatchers(HttpMethod.GET, "/client/*")
                    .hasAnyAuthority(AuthoritiesConst.CLIENT, AuthoritiesConst.BROKER);
        });

        return security.build();
    }
}
