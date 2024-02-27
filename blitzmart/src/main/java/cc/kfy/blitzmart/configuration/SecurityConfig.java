package cc.kfy.blitzmart.configuration;

import cc.kfy.blitzmart.handler.CustomAccessDeniedHandler;
import cc.kfy.blitzmart.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final BCryptPasswordEncoder encoder;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    //private final UserRepositoryImpl userDetailsService;
    private final UserDetailsService userDetailsService;
    private static final String[] PUBLIC_URLS = {};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> {
                            session
                                    .sessionCreationPolicy(STATELESS);
                        }
                )
                .exceptionHandling((exception) -> {
                            exception
                                    .accessDeniedHandler(customAccessDeniedHandler)
                                    .authenticationEntryPoint(customAuthenticationEntryPoint);
                        }
                )
                .authorizeHttpRequests((authorize) -> {
                            authorize
                                    .requestMatchers(PUBLIC_URLS).permitAll()
                                    .requestMatchers(DELETE, "user/delete/**").hasAuthority("DELETE:USER")
                                    .requestMatchers(DELETE, "customer/delete/**").hasAuthority("DELETE:CUSTOMER")
                                    .anyRequest().authenticated();
                        }
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(encoder);
        return new ProviderManager(authenticationProvider);
    }
}
