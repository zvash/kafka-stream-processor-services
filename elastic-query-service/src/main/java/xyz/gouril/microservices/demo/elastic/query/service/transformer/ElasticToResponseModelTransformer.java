package xyz.gouril.microservices.demo.elastic.query.service.transformer;

import org.springframework.stereotype.Component;
import xyz.gouril.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import xyz.gouril.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModel;

import java.util.List;

@Component
public class ElasticToResponseModelTransformer {

    public ElasticQueryServiceResponseModel getResponseModel(TwitterIndexModel indexModel) {
        return ElasticQueryServiceResponseModel.builder()
                .id(indexModel.getId())
                .text(indexModel.getText())
                .userId(indexModel.getUserId())
                .createdAt(indexModel.getCreatedAt())
                .build();
    }

    public List<ElasticQueryServiceResponseModel> getResponseModels(List<TwitterIndexModel> indexModels) {
        return indexModels.stream().map(this::getResponseModel).toList();
    }

}
