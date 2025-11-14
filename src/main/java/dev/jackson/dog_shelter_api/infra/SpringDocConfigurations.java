package dev.jackson.dog_shelter_api.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;


@Configuration
public class SpringDocConfigurations {
    @Bean
     public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Dog Shelter API")
                .description("You can make CRUD actions with this API"));
    }
}
