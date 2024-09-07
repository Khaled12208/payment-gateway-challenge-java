package com.checkout.payment.gateway.configuration;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@Configuration
public class ApplicationConfiguration {

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public ServletContextInitializer servletContextInitializer() {
    return new ServletContextInitializer() {
      @Override
      public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setSessionTimeout(30);
      }
    };
  }
}
