package com.perc.challenge.taskmanager.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Task Manager API",
                description = "Task Manager API docs",
                version = "v1.0"
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "LocalHost:8080")
        }
)
public class SwaggerConfig {
}
