package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class CurrencyService {
    private static final String EXCHANGE_RATE_API = "https://api.nbp.pl/api/exchangerates/rates/a/eur/";

    public BigDecimal getEuroToPlnRate() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(EXCHANGE_RATE_API, String.class);
            JsonNode root = new ObjectMapper().readTree(response);
            return new BigDecimal(root.path("rates").get(0).path("mid").asText());
        } catch (Exception e) {
            // Fallback rate if API fails
            return new BigDecimal("4.50");
        }
    }
}