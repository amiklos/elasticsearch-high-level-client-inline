package com.examples.elastic.search.elasticsearch;

import com.examples.elastic.search.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequest;
import org.elasticsearch.script.mustache.SearchTemplateResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ElasticSearchProductClient {

    private static final String PRODUCTS_INDEX = "product";
    private static final String SCRIPT_PARAM_NAME = "name";

    @Value("${elasticsearch.host}")
    private String host;
    @Value("${elasticsearch.port}")
    private int port;
    @Value("${elasticsearch.protocol}")
    private String protocol;

    private RestHighLevelClient restHighLevelClient;

    @PostConstruct
    public void init() {
        restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost(host, 9200, protocol)));
    }

    public List<Product> getProductsByName(String productName) throws IOException {
        Map<String, Object> scriptParams = new HashMap<>();
        scriptParams.put(SCRIPT_PARAM_NAME, productName);

        SearchTemplateRequest searchTemplateRequest = new SearchTemplateRequest();
        searchTemplateRequest.setScript("search_products_by_name");
        searchTemplateRequest.setScriptType(ScriptType.STORED);
        searchTemplateRequest.setScriptParams(scriptParams);
        searchTemplateRequest.setRequest(new SearchRequest(PRODUCTS_INDEX));

        SearchTemplateResponse searchResponse = restHighLevelClient.searchTemplate(searchTemplateRequest, RequestOptions.DEFAULT);

        return searchResponseToProducts(searchResponse);
    }

    private List<Product> searchResponseToProducts(SearchTemplateResponse searchResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Product> products = new ArrayList<>();
        for (SearchHit searchHits : searchResponse.getResponse().getHits()) {
            products.add(objectMapper.readValue(searchHits.getSourceAsString(), Product.class));
        }
        return products;
    }


    @PreDestroy
    public void destroy() throws IOException {
        restHighLevelClient.close();
    }


}
