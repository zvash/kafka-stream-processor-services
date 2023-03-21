package xyz.gouril.microservices.demo.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import org.springframework.data.elasticsearch.support.HttpHeaders;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Configuration
@EnableElasticsearchRepositories(basePackages = "xyz.gouril.microservices.demo.elastic")
public class ElasticsearchConfig {

    private final ElasticConfigData elasticConfigData;

    public ElasticsearchConfig(ElasticConfigData elasticConfigData) {
        this.elasticConfigData = elasticConfigData;
    }

//    @Override
//    public ClientConfiguration clientConfiguration() {
//        UriComponents serverUri = UriComponentsBuilder.fromHttpUrl(elasticConfigData.getConnectionUrl()).build();
//        return ClientConfiguration.builder()
//                .connectedTo(serverUri.getHost() + ":" + serverUri.getPort())
//                .withConnectTimeout(elasticConfigData.getConnectionTimeoutMs())
//                .withSocketTimeout(elasticConfigData.getSocketTimeoutMs())
//                .build();
//    }

    @Bean
    public RestClient getRestClient() {
        UriComponents serverUri = UriComponentsBuilder.fromHttpUrl(elasticConfigData.getConnectionUrl()).build();
        return RestClient.builder(new HttpHost(
                Objects.requireNonNull(serverUri.getHost()),
                serverUri.getPort(),
                serverUri.getScheme()
        )).setRequestConfigCallback(
                requestConfigBuilder ->
                        requestConfigBuilder
                                .setConnectTimeout(elasticConfigData.getConnectTimeoutMs())
                                .setSocketTimeout(elasticConfigData.getSocketTimeoutMs())
        ).build();
    }

    @Bean
    public ElasticsearchTransport getElasticsearchTransport() {
        return new RestClientTransport(
                getRestClient(), new JacksonJsonpMapper());
    }


    @Bean
    public ElasticsearchClient getElasticsearchClient(){
        ElasticsearchClient client = new ElasticsearchClient(getElasticsearchTransport());
        return client;
    }
}
