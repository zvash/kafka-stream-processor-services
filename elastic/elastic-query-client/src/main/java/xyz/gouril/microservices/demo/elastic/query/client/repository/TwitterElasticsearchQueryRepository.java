package xyz.gouril.microservices.demo.elastic.query.client.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import xyz.gouril.microservices.demo.elastic.model.index.impl.TwitterIndexModel;

import java.util.List;

@Repository
public interface TwitterElasticsearchQueryRepository extends ElasticsearchRepository<TwitterIndexModel, String> {

    List<TwitterIndexModel> findByText(String text);

}
