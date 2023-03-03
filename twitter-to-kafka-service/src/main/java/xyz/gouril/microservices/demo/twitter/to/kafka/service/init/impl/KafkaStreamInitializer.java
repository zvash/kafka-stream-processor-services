package xyz.gouril.microservices.demo.twitter.to.kafka.service.init.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import xyz.gouril.microservices.demo.config.KafkaConfigData;
import xyz.gouril.microservices.demo.kafka.admin.client.KafkaAdminClient;
import xyz.gouril.microservices.demo.twitter.to.kafka.service.init.StreamInitializer;

@Component
public class KafkaStreamInitializer implements StreamInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamInitializer.class);

    private final KafkaConfigData kafkaConfigData;
    private final KafkaAdminClient kafkaAdminClient;

    public KafkaStreamInitializer(KafkaConfigData kafkaConfigData, KafkaAdminClient kafkaAdminClient) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaAdminClient = kafkaAdminClient;
    }

    @Override
    public void init() {
        kafkaAdminClient.createTopics();
        kafkaAdminClient.checkSchemaRegistryIsRunning();
        LOG.info("Topics with name {} is ready for operations.", kafkaConfigData.getTopicNamesToCreate().toArray());
    }
}
