package xyz.gouril.microservices.demo.elastic.query.client.service;

import xyz.gouril.microservices.demo.elastic.model.index.IndexModel;

import java.util.List;

public interface ElasticQueryClient<T extends IndexModel> {

    T getIndexModelById(String id);

    List<T> getIndexModelsByText(String text);

    List<T> getAllIndexModels();

    void deleteAllIndexModels();

}
