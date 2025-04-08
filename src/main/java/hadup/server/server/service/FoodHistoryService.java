package hadup.server.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hadup.server.server.dto.response.FoodHistoryResponse;
import hadup.server.server.dto.response.FoodHistoryTotalResponse;
import hadup.server.server.entity.Food;
import hadup.server.server.entity.FoodHistory;
import hadup.server.server.entity.Plan;
import hadup.server.server.exception.AppException;
import hadup.server.server.exception.ErrorCode;
import hadup.server.server.mapper.FoodHistoryMapper;
import hadup.server.server.mapper.FoodMapper;
import hadup.server.server.repository.FoodHistoryRepository;
import hadup.server.server.repository.FoodRepository;
import hadup.server.server.repository.PlanRepository;
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
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FoodHistoryService {
    FoodHistoryRepository foodHistoryRepository;
    FoodHistoryMapper foodHistoryMapper;
    PlanRepository planRepository;
    public List<FoodHistoryTotalResponse> getFoodHistoryStatic(Date from , Date to) {
        List<FoodHistoryTotalResponse> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(from);

        while (!calendar.getTime().after(to)) {
            Date currentDate = calendar.getTime();
            list.add(getFoodHistoryByDate(currentDate));
            calendar.add(Calendar.DATE, 1); // tăng 1 ngày
        }

        return list;
    }
    public int findDaysUntilFirstNullFoodHistory() {
        // Lấy ngày hôm qua
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Plan plan = planRepository.findPlanByUser_Email("binh@gmail.com").orElseThrow(() -> new AppException(ErrorCode.PLAN_NOTEXISTED));
        int cnt = 0;
        while (true) {
            Date currentDate = calendar.getTime();

            // Gọi hàm để kiểm tra
            FoodHistoryTotalResponse response = getFoodHistoryByDate(currentDate);

            if (response == null || !isValid(plan, response)) {
                break; // Trả về số ngày đã kiểm tra
            }

            // Giảm ngày đi 1 để kiểm tra ngày trước đó
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            cnt++;
        }

        return cnt; // Không tìm thấy ngày nào trả về null trong khoảng thời gian kiểm tra
    }
    public FoodHistoryTotalResponse getFoodHistoryByDate(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Date startOfDay = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endOfDay = Date.from(localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<FoodHistory> listFoodHistory = foodHistoryRepository.findFoodHistoriesByCreateAtBetween(startOfDay, endOfDay);
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
    private boolean isValid(Plan plan, FoodHistoryTotalResponse real){
        if (Math.abs(plan.getCalories() - real.getTotalCalories()) > 150)
            return false;
        else if (Math.abs(plan.getFat() - real.getTotalFat()) > 20 ||
        Math.abs(plan.getSugar() - real.getTotalSugar()) > 20 ||
        Math.abs(plan.getProtein() - real.getTotalProtein()) > 20 ||
        Math.abs(plan.getFiber() - real.getTotalFiber()) > 20)
            return false;
        return true;
    }
}
