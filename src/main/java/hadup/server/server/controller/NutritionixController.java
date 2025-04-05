package hadup.server.server.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
@RestController
class NutritionixController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/calories")
    public String getCalories() {
        try {
            // Create RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

            // Set up the URL
            String url = "https://trackapi.nutritionix.com/v2/natural/nutrients";

            // Set up the headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-app-id", "f8c5699c");  // Replace with your actual app ID
            headers.set("x-app-key", "184684e6917b5ddec39fe15d66697533");  // Replace with your actual app key

            // Set up the request body
            String requestBody = "{\"query\":\"grape\"}";

            // Create the request entity
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            // Make the POST request
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            // Parse the JSON response
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            // Extract just the calories value from the first food item
            double calories = rootNode.path("foods").get(0).path("nf_calories").asDouble();

            return "Calories in grape: " + calories;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
