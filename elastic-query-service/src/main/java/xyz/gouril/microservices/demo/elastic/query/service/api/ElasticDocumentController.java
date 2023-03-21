package xyz.gouril.microservices.demo.elastic.query.service.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.gouril.microservices.demo.elastic.query.service.business.ElasticQueryService;
import xyz.gouril.microservices.demo.elastic.query.service.model.ElasticQueryServiceRequestModel;
import xyz.gouril.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModel;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class ElasticDocumentController {
    private static final Logger LOG = LoggerFactory.getLogger(ElasticDocumentController.class);

    private final ElasticQueryService elasticQueryService;

    public ElasticDocumentController(ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }

    @GetMapping("/")
    public @ResponseBody
    ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments() {
        List<ElasticQueryServiceResponseModel> response = elasticQueryService.getAllDocuments();
        LOG.info("Elasticsearch returned {} of documents", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public @ResponseBody
    ResponseEntity<ElasticQueryServiceResponseModel> getDocumentById(@PathVariable @NotEmpty String id) {
        ElasticQueryServiceResponseModel elasticQueryServiceResponseModel = elasticQueryService.getDocumentById(id);
        LOG.info("Elasticsearch returned a document with id: {}", id);
        return ResponseEntity.ok(elasticQueryServiceResponseModel);
    }

    @PostMapping("/get-documents-by-text")
    public @ResponseBody
    ResponseEntity<List<ElasticQueryServiceResponseModel>> getDocumentsByText(
            @RequestBody @Valid ElasticQueryServiceRequestModel model
    ) {
        List<ElasticQueryServiceResponseModel> response = elasticQueryService.getDocumentsByText(model.getText());
        LOG.info("Elasticsearch returned {} of documents", response.size());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-all")
    public String deleteAll() {
        elasticQueryService.deleteAllDocuments();
        return "done";
    }
}
