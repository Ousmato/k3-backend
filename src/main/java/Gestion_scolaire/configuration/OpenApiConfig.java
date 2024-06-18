package Gestion_scolaire.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

public class OpenApiConfig {
    @Bean
    public OpenAPI artEShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Gestion Scolaire")
                        .description("API pour K3")
                        .version("1.0"));
    }
}
