package com.example.cookingapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   OAuth2AuthenticationSuccessHandler successHandler) throws Exception {
        http
                // Enable CORS (uses your CorsConfigurationSource bean)
                .cors(Customizer.withDefaults())
                // Disable CSRF for APIs
                .csrf(csrf -> csrf.disable())

                // Allow all OPTIONS (CORS preflight) and define your access rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )

                // JWT filter before the UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

                // OAuth2 login with your custom success handler
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(successHandler)
                )

                // Disable default form login
                .formLogin(form -> form.disable());

        return http.build();
    }
}
