package xyz.gouril.microservices.demo.twitter.to.kafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import xyz.gouril.microservices.demo.twitter.to.kafka.service.init.StreamInitializer;
import xyz.gouril.microservices.demo.twitter.to.kafka.service.runner.StreamRunner;

import java.util.Arrays;


@SpringBootApplication
@ComponentScan(basePackages = "xyz.gouril.microservices.demo")
public class TwitterToKafkaServiceApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterToKafkaServiceApplication.class);

    private final StreamInitializer streamInitializer;
    private final StreamRunner streamRunner;

    public TwitterToKafkaServiceApplication(StreamInitializer streamInitializer, StreamRunner streamRunner) {
        this.streamInitializer = streamInitializer;
        this.streamRunner = streamRunner;
    }

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Application started!");
        streamInitializer.init();
        streamRunner.start();
    }
}
