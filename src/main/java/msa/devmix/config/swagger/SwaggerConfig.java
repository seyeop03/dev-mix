package msa.devmix.config.swagger;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(
                        new Components()
                                // accessToken 이라는 스키마 만들어주기
                                .addSecuritySchemes("accessToken", new SecurityScheme()
                                        .name("accessToken")
                                        .type(SecurityScheme.Type.HTTP) //SecurityScheme.Type.HTTP
                                        .in(SecurityScheme.In.HEADER)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                )
                )
                .addSecurityItem(new SecurityRequirement().addList("accessToken"));
    }
}
