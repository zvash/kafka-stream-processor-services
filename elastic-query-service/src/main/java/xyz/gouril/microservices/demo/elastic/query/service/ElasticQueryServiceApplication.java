package xyz.gouril.microservices.demo.elastic.query.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "xyz.gouril.microservices.demo")
public class ElasticQueryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElasticQueryServiceApplication.class, args);
    }
}
