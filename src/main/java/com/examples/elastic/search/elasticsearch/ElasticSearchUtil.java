package com.examples.elastic.search.elasticsearch;

public class ElasticSearchUtil {

    public static String productsByNameQuery() {
        return "{\n" +
                "  \"query\": {\n" +
                "    \"match\": {\n" +
                "      \"name\": \"{{name}}\"\n" +
                "    }\n" +
                "  }\n" +
                "}\n";
    }

}
