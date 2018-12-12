package com.examples.elastic.search.provider;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticsearchRestClientProvider {

    private String host;
    private Integer port;
    private String scheme;


    public RestClient getElasticsearchRestClient() {
        RestClientBuilder builder = RestClient.builder(new HttpHost(host, port, scheme));
        builder.setMaxRetryTimeoutMillis(10000);
        return builder.build();
    }

}
