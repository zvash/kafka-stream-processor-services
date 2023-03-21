package xyz.gouril.microservices.demo.elastic.query.service.model.assembler;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import xyz.gouril.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import xyz.gouril.microservices.demo.elastic.query.service.api.ElasticDocumentController;
import xyz.gouril.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModel;
import xyz.gouril.microservices.demo.elastic.query.service.transformer.ElasticToResponseModelTransformer;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ElasticQueryServiceResponseModelAssembler
        extends RepresentationModelAssemblerSupport<TwitterIndexModel, ElasticQueryServiceResponseModel> {

    private final ElasticToResponseModelTransformer elasticToResponseModelTransformer;

    public ElasticQueryServiceResponseModelAssembler(ElasticToResponseModelTransformer elasticToResponseModelTransformer) {
        super(ElasticDocumentController.class, ElasticQueryServiceResponseModel.class);
        this.elasticToResponseModelTransformer = elasticToResponseModelTransformer;
    }

    @Override
    public ElasticQueryServiceResponseModel toModel(TwitterIndexModel twitterIndexModel) {
        ElasticQueryServiceResponseModel responseModel = elasticToResponseModelTransformer.getResponseModel(twitterIndexModel);
        responseModel.add(
                linkTo(methodOn(ElasticDocumentController.class)
                        .getDocumentById((twitterIndexModel.getId()))
                ).withSelfRel()
        );
        responseModel.add(
                linkTo(methodOn(ElasticDocumentController.class).getAllDocuments())
                        .withRel("documents")
        );
        return  responseModel;
    }

    public List<ElasticQueryServiceResponseModel> toModels(List<TwitterIndexModel> twitterIndexModels) {
        return twitterIndexModels.stream().map(this::toModel).toList();
    }
}
