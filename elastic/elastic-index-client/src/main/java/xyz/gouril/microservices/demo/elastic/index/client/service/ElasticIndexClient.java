package xyz.gouril.microservices.demo.elastic.index.client.service;

import xyz.gouril.microservices.demo.elastic.model.index.IndexModel;

import java.util.List;

public interface ElasticIndexClient<T extends IndexModel> {
    List<String> save(List<T> documents);
}
