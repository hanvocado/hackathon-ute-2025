package hadup.server.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hadup.server.server.dto.ApiResponse;
import hadup.server.server.entity.FoodHistory;
import hadup.server.server.service.FoodHistoryService;
import hadup.server.server.service.SalusService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/food-history")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class FoodHistoryController {
    FoodHistoryService foodHistoryService;
    @GetMapping
    public ApiResponse<List<FoodHistory>> getFoodHistory(LocalDate date) {
        return ApiResponse.<List<FoodHistory>>builder()
                .result(foodHistoryService.getFoodHistoryByDate(date))
                .build();
    }
}
