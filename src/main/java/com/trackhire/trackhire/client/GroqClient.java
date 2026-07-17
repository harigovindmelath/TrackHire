package com.trackhire.trackhire.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class GroqClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    public String extractJobDetails(String jobDescriptionText) {
        String prompt = buildPrompt(jobDescriptionText);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = Map.of(
                "model", "llama-3.3-70b-versatile",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        Map response = restTemplate.postForObject(apiUrl, requestEntity, Map.class);

        List<Map> choices = (List<Map>) response.get("choices");
        Map firstChoice = choices.get(0);
        Map message = (Map) firstChoice.get("message");
        String content = (String) message.get("content");

        return content;
    }

    private String buildPrompt(String jobDescriptionText) {
        return "You are a job description parser. Extract structured information from the job description below.\n\n"
                + "Respond with ONLY a valid JSON object, no explanation, no markdown formatting, no code fences.\n\n"
                + "Use exactly these fields, with these exact types:\n"
                + "- companyName: string\n"
                + "- role: string\n"
                + "- skills: a single comma-separated string (not an array), e.g. \"Java, Spring Boot, MySQL\"\n"
                + "- location: string\n"
                + "- experienceRequired: a string like \"0-2 years\" (not a number)\n\n"
                + "Job description:\n" + jobDescriptionText;
    }
}