package xyz.gouril.microservices.demo.twitter.to.kafka.service.transformer;

import org.springframework.stereotype.Component;
import twitter4j.Status;
import xyz.gouril.microservices.demo.kafka.avro.model.TwitterAvroModel;

@Component
public class TwitterStatusToAvroTransformer {

    public TwitterAvroModel getTwitterAvroModelFromStatus(Status status) {
        return TwitterAvroModel
                .newBuilder()
                .setId(status.getId())
                .setUserId(status.getUser().getId())
                .setText(status.getText())
                .setCreatedAt(status.getCreatedAt().getTime())
                .build();
    }

}
