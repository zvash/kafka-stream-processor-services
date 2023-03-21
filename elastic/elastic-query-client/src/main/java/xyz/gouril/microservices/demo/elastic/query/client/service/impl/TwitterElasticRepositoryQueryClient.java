package xyz.gouril.microservices.demo.elastic.query.client.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import xyz.gouril.microservices.demo.common.util.CollectionsUtil;
import xyz.gouril.microservices.demo.config.ElasticConfigData;
import xyz.gouril.microservices.demo.config.ElasticQueryConfigData;
import xyz.gouril.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import xyz.gouril.microservices.demo.elastic.query.client.exception.ElasticQueryClientException;
import xyz.gouril.microservices.demo.elastic.query.client.repository.TwitterElasticsearchQueryRepository;
import xyz.gouril.microservices.demo.elastic.query.client.service.ElasticQueryClient;

import java.util.List;
import java.util.Optional;

@Primary
@Service
public class TwitterElasticRepositoryQueryClient implements ElasticQueryClient<TwitterIndexModel> {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticRepositoryQueryClient.class);
    
    private final TwitterElasticsearchQueryRepository twitterElasticsearchQueryRepository;

    public TwitterElasticRepositoryQueryClient(TwitterElasticsearchQueryRepository twitterElasticsearchQueryRepository) {
        this.twitterElasticsearchQueryRepository = twitterElasticsearchQueryRepository;
    }

    @Override
    public TwitterIndexModel getIndexModelById(String id) {
        Optional<TwitterIndexModel> searchResult = twitterElasticsearchQueryRepository.findById(id);
        if (searchResult.isEmpty()) {
            throw new ElasticQueryClientException("No document was found for elasticsearch with id: " + id);
        }
        LOG.info("Document with id {} retrieved successfully", searchResult.get().getId());
        return searchResult.get();
    }

    @Override
    public List<TwitterIndexModel> getIndexModelsByText(String text) {
        List<TwitterIndexModel> searchResult = twitterElasticsearchQueryRepository.findByText(text);
        LOG.info("{} of documents with text {} retrieved successfully", searchResult.size(), text);
        return searchResult;
    }

    @Override
    public List<TwitterIndexModel> getAllIndexModels() {
        Iterable<TwitterIndexModel> searchResult = twitterElasticsearchQueryRepository.findAll();
        List<TwitterIndexModel> indexModels = CollectionsUtil.getInstance().getListFromIterable(searchResult);
        LOG.info("{} of documents retrieved successfully", indexModels.size());
        return indexModels;
    }

    @Override
    public void deleteAllIndexModels() {
        twitterElasticsearchQueryRepository.deleteAll();
    }
}
