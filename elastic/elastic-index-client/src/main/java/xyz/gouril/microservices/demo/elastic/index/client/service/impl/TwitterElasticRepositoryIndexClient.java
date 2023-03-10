package xyz.gouril.microservices.demo.elastic.index.client.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import xyz.gouril.microservices.demo.elastic.index.client.repository.TwitterElasticsearchIndexRepository;
import xyz.gouril.microservices.demo.elastic.index.client.service.ElasticIndexClient;
import xyz.gouril.microservices.demo.elastic.model.index.impl.TwitterIndexModel;

import java.util.List;

@Service
@ConditionalOnProperty(name = "elastic-index-client.enable-repository-index-client", havingValue = "true", matchIfMissing = true)
public class TwitterElasticRepositoryIndexClient implements ElasticIndexClient<TwitterIndexModel> {
    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticRepositoryIndexClient.class);

    private final TwitterElasticsearchIndexRepository twitterElasticsearchIndexRepository;

    public TwitterElasticRepositoryIndexClient(TwitterElasticsearchIndexRepository twitterElasticsearchIndexRepository) {
        this.twitterElasticsearchIndexRepository = twitterElasticsearchIndexRepository;
    }

    @Override
    public List<String> save(List<TwitterIndexModel> documents) {
        List<TwitterIndexModel> repositoryResponse =
                (List<TwitterIndexModel>) twitterElasticsearchIndexRepository.saveAll(documents);
        List<String> ids = repositoryResponse.stream().map(TwitterIndexModel::getId).toList();
        LOG.info("Documents indexed successfully with type: {} and ids:  {}", TwitterIndexModel.class.getName(), ids);
        return ids;
    }
}
