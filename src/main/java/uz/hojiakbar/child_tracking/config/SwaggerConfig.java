package uz.hojiakbar.child_tracking.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Child Tracking API")
                        .version("1.0.0")
                        .description("Child tracking system"))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("1. Auth")
                .pathsToMatch("/api/auth/**", "/api/v1/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi parentApi() {
        return GroupedOpenApi.builder()
                .group("2. Parent")
                .pathsToMatch("/api/v1/parent/**")
                .build();
    }

    @Bean
    public GroupedOpenApi childApi() {
        return GroupedOpenApi.builder()
                .group("3. Child")
                .pathsToMatch("/api/v1/child/**")
                .build();
    }

    @Bean
    public GroupedOpenApi locationApi() {
        return GroupedOpenApi.builder()
                .group("4. Location & Geofence")
                .pathsToMatch("/api/v1/location/**", "/api/v1/geofences/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("5. Super Admin")
                .pathsToMatch("/api/super-admin/**")
                .build();
    }
}