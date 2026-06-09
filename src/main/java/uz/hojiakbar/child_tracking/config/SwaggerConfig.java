package uz.hojiakbar.child_tracking.config;


import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.web.method.HandlerMethod;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.media.StringSchema;

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
                                        .bearerFormat("JWT"))
                .addParameters("X-Device-ID",    new Parameter().in("header").name("X-Device-ID").schema(new StringSchema()).description("Qurilma ID si"))
                .addParameters("X-Device-Name",  new Parameter().in("header").name("X-Device-Name").schema(new StringSchema()).description("Qurilma nomi"))
                .addParameters("X-Platform",     new Parameter().in("header").name("X-Platform").schema(new StringSchema()).description("android / ios"))
                .addParameters("X-App-Version",  new Parameter().in("header").name("X-App-Version").schema(new StringSchema()).description("App versiyasi")));


    }



    @Bean
    public OperationCustomizer globalHeaders() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            operation.addParametersItem(
                    new Parameter()
                            .in("header")
                            .name("X-Device-ID")
                            .schema(new StringSchema())
                            .description("Qurilma ID si")
                            .required(false)
            );
            operation.addParametersItem(
                    new Parameter()
                            .in("header")
                            .name("X-Device-Name")
                            .schema(new StringSchema())
                            .description("Qurilma nomi")
                            .required(false)
            );
            operation.addParametersItem(
                    new Parameter()
                            .in("header")
                            .name("X-Platform")
                            .schema(new StringSchema())
                            .description("android / ios")
                            .required(false)
            );
            operation.addParametersItem(
                    new Parameter()
                            .in("header")
                            .name("X-App-Version")
                            .schema(new StringSchema())
                            .description("App versiyasi")
                            .required(false)
            );
            return operation;
        };
    }




    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("1. Auth")
                .pathsToMatch("/api/auth/**", "/api/v1/auth/**")
                .addOperationCustomizer(globalHeaders())
                .build();
    }

    @Bean
    public GroupedOpenApi parentApi() {
        return GroupedOpenApi.builder()
                .group("2. Parent")
                .pathsToMatch(
                        "/api/v1/parent/**" ,
                        "/api/tasks/**",
                        "/api/v1/notification/**",
                        "/api/v1/alert/**",
                        "/api/v1/sos/*/resolve")
                .addOperationCustomizer(globalHeaders())
                .build();
    }
    @Bean
    public GroupedOpenApi childApi() {
        return GroupedOpenApi.builder()
                .group("3. Child")
                .pathsToMatch(
                        "/api/v1/child/**",
                        "/api/tasks/my",
                        "/api/tasks/*/done",
                        "/api/v1/notification/**",
                        "/api/v1/sos/trigger",
                        "/api/v1/sos/history",
                        "/api/v1/alerts/**")
                .addOperationCustomizer(globalHeaders())
                .build();
    }

    @Bean
    public GroupedOpenApi locationApi() {
        return GroupedOpenApi.builder()
                .group("4. Location & Geofence")
                .pathsToMatch("/api/v1/location/**", "/api/v1/geofences/**")
                .addOperationCustomizer(globalHeaders())
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("5. Super Admin")
                .pathsToMatch("/api/super-admin/**")
                .addOperationCustomizer(globalHeaders())
                .build();
    }




}