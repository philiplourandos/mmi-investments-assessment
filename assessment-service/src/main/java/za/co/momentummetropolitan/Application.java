package za.co.momentummetropolitan;

import java.time.LocalDate;
import java.util.function.Supplier;
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
        security.csrf().disable().authorizeHttpRequests(authz -> { authz
                .requestMatchers(HttpMethod.GET, "/client/*")
                    .hasAnyAuthority(AuthoritiesConst.CLIENT, AuthoritiesConst.BROKER)
                .requestMatchers(HttpMethod.GET, "/client/products/*")
                    .hasAnyAuthority(AuthoritiesConst.CLIENT, AuthoritiesConst.BROKER)
                .requestMatchers(HttpMethod.POST, "/client/withdraw/**")
                    .hasAnyAuthority(AuthoritiesConst.CLIENT);
        });

        return security.build();
    }

    @Bean
    public Supplier<LocalDate> defaultDate() {
        return () -> LocalDate.now();
    }
}
