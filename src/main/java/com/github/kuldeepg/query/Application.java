package com.github.kuldeepg.query;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.github.kuldeepg")
@EntityScan(basePackages = "com.github.kuldeepg")
@ComponentScan(basePackages = "com.github.kuldeepg")
@IntegrationComponentScan
@EnableAutoConfiguration
@EnableScheduling
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class);
  }
}
