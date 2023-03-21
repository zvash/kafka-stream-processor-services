package xyz.gouril.microservices.demo.elastic.model.index.impl;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;
import xyz.gouril.microservices.demo.elastic.model.index.IndexModel;

import java.time.LocalDateTime;

@Data
@Builder
@Document(indexName = "twitter-index")
public class TwitterIndexModel implements IndexModel {

    @JsonProperty
    private String id;

    @JsonProperty
    private Long userId;

    @JsonProperty
    private String text;

    @Field(type = FieldType.Date, format = {}, pattern = "uuuu-MM-dd'T'HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd'T'HH:mm:ss")
    @JsonProperty
    private LocalDateTime createdAt;
}
