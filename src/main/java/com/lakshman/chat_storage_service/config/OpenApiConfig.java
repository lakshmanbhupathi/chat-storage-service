package com.lakshman.chat_storage_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Chat Storage API", 
        version = "1.0", 
        description = "API for storing chat sessions and messages"
    ),
    security = @SecurityRequirement(name = "X-API-KEY")
)
@SecurityScheme(
    name = "X-API-KEY",
    type = SecuritySchemeType.APIKEY,
    in = SecuritySchemeIn.HEADER,
    paramName = "X-API-KEY",
    description = "API Key for authentication. Enter your API key to authorize requests."
)
public class OpenApiConfig {
}
