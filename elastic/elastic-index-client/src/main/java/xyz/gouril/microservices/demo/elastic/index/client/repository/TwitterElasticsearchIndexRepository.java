package xyz.gouril.microservices.demo.elastic.index.client.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import xyz.gouril.microservices.demo.elastic.model.index.impl.TwitterIndexModel;

@Repository
public interface TwitterElasticsearchIndexRepository extends ElasticsearchRepository<TwitterIndexModel, String> {

}
