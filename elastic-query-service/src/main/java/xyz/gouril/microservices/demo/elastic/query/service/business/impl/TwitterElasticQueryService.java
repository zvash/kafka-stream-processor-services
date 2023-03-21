package xyz.gouril.microservices.demo.elastic.query.service.business.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import xyz.gouril.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import xyz.gouril.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import xyz.gouril.microservices.demo.elastic.query.service.business.ElasticQueryService;
import xyz.gouril.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModel;
import xyz.gouril.microservices.demo.elastic.query.service.model.assembler.ElasticQueryServiceResponseModelAssembler;

import java.util.List;

@Service
public class TwitterElasticQueryService implements ElasticQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticQueryService.class);

    private final ElasticQueryClient<TwitterIndexModel> elasticQueryClient;
    private final ElasticQueryServiceResponseModelAssembler elasticQueryServiceResponseModelAssembler;

    public TwitterElasticQueryService(ElasticQueryClient<TwitterIndexModel> elasticQueryClient,
                                      ElasticQueryServiceResponseModelAssembler elasticQueryServiceResponseModelAssembler) {
        this.elasticQueryClient = elasticQueryClient;
        this.elasticQueryServiceResponseModelAssembler = elasticQueryServiceResponseModelAssembler;
    }

    @Override
    public ElasticQueryServiceResponseModel getDocumentById(String id) {
        LOG.info("Querying elasticsearch for document with id: {}", id);
        return elasticQueryServiceResponseModelAssembler.toModel(elasticQueryClient.getIndexModelById(id));
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getDocumentsByText(String text) {
        LOG.info("Querying elasticsearch for documents with text: {}", text);
        return elasticQueryServiceResponseModelAssembler.toModels(elasticQueryClient.getIndexModelsByText(text));
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getAllDocuments() {
        LOG.info("Querying elasticsearch for all documents");
        return elasticQueryServiceResponseModelAssembler.toModels(elasticQueryClient.getAllIndexModels());
    }

    @Override
    public void deleteAllDocuments() {
        elasticQueryClient.deleteAllIndexModels();
    }
}
