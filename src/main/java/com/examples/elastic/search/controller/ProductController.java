package com.examples.elastic.search.controller;

import com.examples.elastic.search.elasticsearch.ElasticSearchProductClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@RequestMapping("/product-service")
public class ProductController {

    private static final String ATTRIBUTE_PRODUCTS = "products";

    @Autowired
    private ElasticSearchProductClient elasticSearchProductClient;

    @GetMapping
    public String home() {
        return "index";
    }


    @PostMapping(value = "/search", produces = MediaType.TEXT_PLAIN_VALUE)
    public ModelAndView searchProductsByName(@RequestParam String name) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(ATTRIBUTE_PRODUCTS, elasticSearchProductClient.getProductsByName(name));
        modelAndView.setViewName("index");
        return modelAndView;
    }
}
