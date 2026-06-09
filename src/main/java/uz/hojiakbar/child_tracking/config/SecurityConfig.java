package uz.hojiakbar.child_tracking.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**", "/swagger-ui.html",
                                "/v3/api-docs/**", "/v3/api-docs",
                                "/swagger-resources/**", "/webjars/**",
                                "/error"
                        ).permitAll()

                         .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/v1/child/register").permitAll()

                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/parent/**").hasRole("PARENT")
                        .requestMatchers("/api/v1/location/send").hasRole("CHILD")
                        .requestMatchers("/api/v1/location/**").hasRole("PARENT")
                        .requestMatchers("/api/v1/geofences/**").hasRole("PARENT")
                        .requestMatchers(HttpMethod.POST, "/api/tasks").hasRole("PARENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/tasks/**").hasRole("PARENT")
                        .requestMatchers("/api/tasks/my").hasRole("CHILD")
                        .requestMatchers("/api/tasks/*/done").hasRole("CHILD")
                        .requestMatchers("/api/tasks/**").authenticated()
                        .requestMatchers("/api/v1/notification/send").hasAnyRole("PARENT", "ADMIN")
                        .requestMatchers("/api/v1/notification/**").authenticated()
                        .anyRequest().authenticated()
                ).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}