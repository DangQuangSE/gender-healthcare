package com.S_Health.GenderHealthCare.config;

import com.S_Health.GenderHealthCare.common.security.JwtAuthenticationFilter;
import com.S_Health.GenderHealthCare.common.security.RestAccessDeniedHandler;
import com.S_Health.GenderHealthCare.common.security.RestAuthenticationEntryPoint;
import com.S_Health.GenderHealthCare.service.authentication.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationService authenticationService;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;
    private final RestAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            @Lazy AuthenticationService authenticationService,
            RestAuthenticationEntryPoint authenticationEntryPoint,
            RestAccessDeniedHandler accessDeniedHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationService = authenticationService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CorsConfigurationSource corsConfigurationSource) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/error", "/favicon.ico").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**")
                        .permitAll()
                        .requestMatchers("/ws/chat/**").permitAll()
                        .requestMatchers("/api/auth/**", "/api/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/config/**").permitAll()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/services/**",
                                "/api/v1/specializations/**",
                                "/api/v1/rooms/**",
                                "/api/v1/tags/**",
                                "/api/v1/consultants/**",
                                "/api/services/**",
                                "/api/specializations/**",
                                "/api/rooms/**",
                                "/api/tags/**",
                                "/api/consultants/**",
                                "/api/comment/blog/**",
                                "/api/feedback/**",
                                "/api/schedules/**",
                                "/api/v1/schedules/**",
                                "/api/v1/treatment-protocols/**",
                                "/api/treatment/**")
                        .permitAll()
                        .requestMatchers(
                                "/api/config/**",
                                "/api/v1/config/**",
                                "/api/v1/services/**",
                                "/api/v1/specializations/**",
                                "/api/v1/rooms/**",
                                "/api/v1/tags/**",
                                "/api/booking-reports/**",
                                "/api/financial-reports/**")
                        .hasAnyRole("ADMIN", "SUPER_ADMIN", "STAFF")
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/blog/my-blogs/**",
                                "/api/blog/admin/**")
                        .authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/blog/**").permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/chat/start",
                                "/api/chat/send",
                                "/api/chat/sessions/*/verify",
                                "/api/chat/sessions/*/mark-read",
                                "/api/payment/vnpay/**",
                                "/api/blog/*/like")
                        .permitAll()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/chat/sessions/*/messages",
                                "/api/chat/sessions/*/unread-count")
                        .permitAll()
                        .requestMatchers("/api/admin/**", "/api/v1/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/payment/vnpay/vnpay-return").permitAll()
                        .anyRequest().authenticated())
                .userDetailsService(authenticationService)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
