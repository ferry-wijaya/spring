package cc.kfy.blitzmart.configuration;

import cc.kfy.blitzmart.handler.CustomAccessDeniedHandler;
import cc.kfy.blitzmart.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final BCryptPasswordEncoder encoder;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    //private final UserRepositoryImpl userDetailsService;
    private final UserDetailsService userDetailsService;
    private static final String[] PUBLIC_URLS = {"/user/login/**", "/user/register/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf((csrf)->csrf.disable())
//                .cors((cors)->cors.disable())
//                .sessionManagement((session) -> {
//                            session
//                                    .sessionCreationPolicy(STATELESS);
//                        }
//                )
//                .authorizeHttpRequests((authorize) -> {
//                            authorize
//                                    .requestMatchers(PUBLIC_URLS).permitAll()
//                                    .requestMatchers(DELETE, "user/delete/**").hasAnyAuthority("DELETE:USER")
//                                    .requestMatchers(DELETE, "customer/delete/**").hasAnyAuthority("DELETE:CUSTOMER")
//                                    .anyRequest().authenticated();
//                        }
//                )
//                .exceptionHandling((exception) -> {
//                            exception
//                                    .accessDeniedHandler(customAccessDeniedHandler)
//                                    .authenticationEntryPoint(customAuthenticationEntryPoint);
//                        }
//                );
        http.csrf((csrf)-> csrf.disable());
        http.cors((cors)-> cors.disable());
        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests((authorize)-> authorize.requestMatchers(PUBLIC_URLS).permitAll());
        http.authorizeHttpRequests((authorize)-> authorize.requestMatchers(HttpMethod.DELETE, "/user/delete/**").hasAnyAuthority("DELETE:USER"));
        http.authorizeHttpRequests((authorize)-> authorize.requestMatchers(HttpMethod.DELETE, "/customer/delete/**").hasAnyAuthority("DELETE:CUSTOMER"));
        http.exceptionHandling((ex)-> ex.accessDeniedHandler(customAccessDeniedHandler).authenticationEntryPoint(customAuthenticationEntryPoint));
        http.authorizeHttpRequests((authorize)-> authorize.anyRequest().authenticated());

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
