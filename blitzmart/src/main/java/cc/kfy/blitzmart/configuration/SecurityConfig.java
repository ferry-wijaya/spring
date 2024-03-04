package cc.kfy.blitzmart.configuration;

import cc.kfy.blitzmart.filter.CustomAuthorizationFilter;
import cc.kfy.blitzmart.handler.CustomAccessDeniedHandler;
import cc.kfy.blitzmart.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
	private final BCryptPasswordEncoder encoder;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	private final UserDetailsService userDetailsService;
	private final CustomAuthorizationFilter customAuthorizationFilter;
	// private static final String[] PUBLIC_URLS = {"/user/login/**", "/user/register/**", "/user/verify/code/**"};
	private static final String[] PUBLIC_URLS = {"/**"};

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable);
		// http.cors(AbstractHttpConfigurer::disable);
		// http.cors(c -> c.configurationSource(corsConfigurationSource()));
		http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.exceptionHandling(e -> e.accessDeniedHandler(customAccessDeniedHandler));
		http.exceptionHandling(e -> e.authenticationEntryPoint(customAuthenticationEntryPoint));
		http.authorizeHttpRequests(r -> r.requestMatchers(PUBLIC_URLS).permitAll());
		http.authorizeHttpRequests(r -> r.requestMatchers(HttpMethod.DELETE, "/user/delete/**").hasAnyAuthority("DELETE:USER"));
		http.authorizeHttpRequests(r -> r.requestMatchers(HttpMethod.DELETE, "/customer/delete/**").hasAnyAuthority("DELETE:CUSTOMER"));
		http.authorizeHttpRequests(r -> r.anyRequest().authenticated());
		//http.addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(encoder);

		return new ProviderManager(authenticationProvider);
	}

	@Autowired
	public void Configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.eraseCredentials(false);
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		var corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:3000", "http://securecapita.org", "http://192.168.1.164", "http://192.168.1.216", "http://100.14.212.97:5000"));
		//corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
						"Accept", "Jwt-Token", "Authorization", "Origin", "Accept", "X-Requested-With",
						"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization",
						"Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "File-Name"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		var source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);
		return source;
	}

    /* Documentation
    https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/web/authentication/UsernamePasswordAuthenticationFilter.html
    https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/AuthenticationManager.html
    https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/AuthenticationProvider.html
    https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/authentication/configurers/userdetails/DaoAuthenticationConfigurer.html
    https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/dao/AbstractUserDetailsAuthenticationProvider.html
    https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/core/userdetails/UserDetailsService.html
     */
}
