package com.aasrivas.journalApp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@Service
public class SentimentAnalysisService {

    @Value("${genai.api.url}")
    private String genaiApiUrl;

    @Value("${genai.api.key}")
    private String genaiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getSentiment(String journalText) {
        try {
            // Build the request body
            JSONObject requestBody = new JSONObject();
            requestBody.put("journals", journalText);

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-api-key", genaiApiKey);

            // Build the request
            HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

            // Make the POST request
            ResponseEntity<String> response = restTemplate.exchange(
                    genaiApiUrl,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                JSONObject responseJson = new JSONObject(response.getBody());
                return responseJson.optString("summary", "No summary returned");
            } else {
                return "Error: " + response.getStatusCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }
}
