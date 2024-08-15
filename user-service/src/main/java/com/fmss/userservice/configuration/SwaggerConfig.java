package com.fmss.userservice.configuration;


import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration

@SecurityScheme(
        name = "Bearer Token",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT"
)
@SecurityScheme(
        name = "OAuth2-google",
        type = SecuritySchemeType.OAUTH2,
        flows = @io.swagger.v3.oas.annotations.security.OAuthFlows(
                authorizationCode = @io.swagger.v3.oas.annotations.security.OAuthFlow(
                        authorizationUrl = "/api/v1/oauth2/authorization/google",
                        tokenUrl = "/api/v1/auth/oauth2/redirect",
                        scopes = {
                                @io.swagger.v3.oas.annotations.security.OAuthScope(name = "email"),
                                @io.swagger.v3.oas.annotations.security.OAuthScope(name = "profile")
                        }
                )
        )
)
@SecurityScheme(
        name = "OAuth2-github",
        type = SecuritySchemeType.OAUTH2,
        flows = @io.swagger.v3.oas.annotations.security.OAuthFlows(
                authorizationCode = @io.swagger.v3.oas.annotations.security.OAuthFlow(
                        authorizationUrl = "/api/v1/oauth2/authorization/github",
                        tokenUrl = "/api/v1/auth/oauth2/redirect",
                        scopes = {
                                @io.swagger.v3.oas.annotations.security.OAuthScope(name = "email"),
                                @io.swagger.v3.oas.annotations.security.OAuthScope(name = "profile")
                        }
                )
        )
)

public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Bearer Token"))
                .addSecurityItem(new SecurityRequirement().addList("OAuth2"))
                .info(new Info()
                        .title("fmss")
                        .version("1.0.0")
                        .description("API documentation for fmss application")
                );
    }
}