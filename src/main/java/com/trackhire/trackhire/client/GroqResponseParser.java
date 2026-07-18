package com.trackhire.trackhire.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackhire.trackhire.exception.GroqServiceException;
import org.springframework.stereotype.Component;

@Component
public class GroqResponseParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ExtractedJobDetails parse(String rawContent) {
        try {
            String cleaned = rawContent.trim();
            // defensively strip markdown code fences if Groq adds them despite instructions
            if (cleaned.startsWith("```")) {
                cleaned = cleaned.replaceAll("```json", "").replaceAll("```", "").trim();
            }
            return objectMapper.readValue(cleaned, ExtractedJobDetails.class);
        } catch (Exception e) {
        throw new GroqServiceException("Failed to parse Groq response: " + e.getMessage());

        }
    }
}