package xyz.gouril.microservices.demo.elastic.index.client.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;
import xyz.gouril.microservices.demo.config.ElasticConfigData;
import xyz.gouril.microservices.demo.elastic.index.client.service.ElasticIndexClient;
import xyz.gouril.microservices.demo.elastic.index.client.util.ElasticIndexUtil;
import xyz.gouril.microservices.demo.elastic.model.index.impl.TwitterIndexModel;

import java.util.List;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name = "elastic-index-client.enable-repository-index-client", havingValue = "false")
public class TwitterElasticIndexClient implements ElasticIndexClient<TwitterIndexModel> {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticIndexClient.class);

    private final ElasticConfigData elasticConfigData;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticIndexUtil<TwitterIndexModel> elasticIndexUtil;

    public TwitterElasticIndexClient(ElasticConfigData elasticConfigData,
                                     ElasticsearchOperations elasticsearchOperations,
                                     ElasticIndexUtil<TwitterIndexModel> elasticIndexUtil) {
        this.elasticConfigData = elasticConfigData;
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticIndexUtil = elasticIndexUtil;
    }

    @Override
    public List<String> save(List<TwitterIndexModel> documents) {
        List<IndexQuery> indexQueries = elasticIndexUtil.getIndexQueries(documents);
        List<String> documentIds = elasticsearchOperations.bulkIndex(
                indexQueries,
                IndexCoordinates.of(elasticConfigData.getIndexName())
        ).stream().map(IndexedObjectInformation::getId).toList();

        LOG.info("Documents indexed successfully with type: {} and ids:  {}", TwitterIndexModel.class.getName(), documentIds);
        return documentIds;
    }
}
