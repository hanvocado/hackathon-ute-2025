package hadup.server.server.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hadup.server.server.dto.ApiResponse;
import hadup.server.server.dto.response.FoodImageResponse;
import hadup.server.server.service.SalusService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/salus")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class SalusController {
    SalusService salusService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/food-image")
    public ApiResponse<FoodImageResponse> postFoodImage(String foodName, MultipartFile file) throws IOException {
        return ApiResponse.<FoodImageResponse>builder()
                .result(salusService.imageToFood(file))
                .build();
    }
}
