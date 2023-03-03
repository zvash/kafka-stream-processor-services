package xyz.gouril.microservices.demo.twitter.to.kafka.service.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import xyz.gouril.microservices.demo.config.KafkaConfigData;
import xyz.gouril.microservices.demo.kafka.avro.model.TwitterAvroModel;
import xyz.gouril.microservices.demo.kafka.producer.service.KafkaProducer;
import xyz.gouril.microservices.demo.twitter.to.kafka.service.transformer.TwitterStatusToAvroTransformer;

@Component
public class TwitterKafkaStatusListener extends StatusAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterKafkaStatusListener.class);

    private final KafkaConfigData kafkaConfigData;
    private final KafkaProducer<Long, TwitterAvroModel> kafkaProducer;
    private final TwitterStatusToAvroTransformer twitterStatusToAvroTransformer;

    public TwitterKafkaStatusListener(KafkaConfigData kafkaConfigData,
                                      KafkaProducer<Long, TwitterAvroModel> kafkaProducer,
                                      TwitterStatusToAvroTransformer twitterStatusToAvroTransformer) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaProducer = kafkaProducer;
        this.twitterStatusToAvroTransformer = twitterStatusToAvroTransformer;
    }

    @Override
    public void onStatus(Status status) {
        LOG.info("Received status text {} sending to kafka topic {}", status.getText(), kafkaConfigData.getTopicName());
        TwitterAvroModel model = twitterStatusToAvroTransformer.getTwitterAvroModelFromStatus(status);
        kafkaProducer.send(kafkaConfigData.getTopicName(), model.getUserId(), model);
    }
}
