package com.checkout.payment.gateway.configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Payment Gateway API")
            .version("1.0.0")
            .description("Responsible for validating requests, storing card information and forwarding payment requests and accepting payment responses to and from the acquiring bank."));
  }

}
