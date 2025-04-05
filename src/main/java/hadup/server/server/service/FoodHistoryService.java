package hadup.server.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hadup.server.server.dto.response.FoodHistoryResponse;
import hadup.server.server.dto.response.FoodHistoryTotalResponse;
import hadup.server.server.entity.Food;
import hadup.server.server.entity.FoodHistory;
import hadup.server.server.exception.AppException;
import hadup.server.server.exception.ErrorCode;
import hadup.server.server.mapper.FoodHistoryMapper;
import hadup.server.server.mapper.FoodMapper;
import hadup.server.server.repository.FoodHistoryRepository;
import hadup.server.server.repository.FoodRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FoodHistoryService {
    FoodHistoryRepository foodHistoryRepository;
    FoodHistoryMapper foodHistoryMapper;
    public List<FoodHistoryTotalResponse> getFoodHistoryStatic(LocalDate from , LocalDate to) {
        List<FoodHistoryTotalResponse> list = new ArrayList<>();
        for (var date = from; !date.isAfter(to); date = date.plusDays(1)) {
            list.add(getFoodHistoryByDate(date));
        }
        return list;
    }
    public FoodHistoryTotalResponse getFoodHistoryByDate(LocalDate date) {
        List<FoodHistory> listFoodHistory = foodHistoryRepository.findFoodHistoriesByCreateAt(date);
        int totalCalories = 0;
        double totalFat = 0;
        double totalSugar = 0;
        double totalProtein = 0;
        double totalFiber = 0;
        for (var foodHistory : listFoodHistory) {
            totalCalories += foodHistory.getFood().getCalories();
            totalFat += foodHistory.getFood().getFat();
            totalSugar += foodHistory.getFood().getSugar();
            totalProtein += foodHistory.getFood().getProtein();
            totalFiber += foodHistory.getFood().getFiber();
        }
        List<FoodHistoryResponse> listFoodHistoryResponse = new ArrayList<>();
        listFoodHistory.forEach(foodHistory -> listFoodHistoryResponse.add(foodHistoryMapper.toResponse(foodHistory))
        );

        return FoodHistoryTotalResponse.builder()
                .foodHistoryList(listFoodHistoryResponse)
                .totalCalories(totalCalories)
                .totalFat(totalFat)
                .totalSugar(totalSugar)
                .totalProtein(totalProtein)
                .totalFiber(totalFiber)
                .build();
    }
}
