package hadup.server.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hadup.server.server.dto.EmailDTO;
import hadup.server.server.dto.request.PlanningRequest;
import hadup.server.server.dto.request.SummaryRequest;
import hadup.server.server.dto.response.PlanningResponse;
import hadup.server.server.entity.*;
import hadup.server.server.exception.AppException;
import hadup.server.server.exception.ErrorCode;
import hadup.server.server.mapper.PlanMapper;
import hadup.server.server.mapper.ProfileUserMapper;
import hadup.server.server.repository.*;
import lombok.AccessLevel;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
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
public class SalusService {
    PlanRepository planRepository;
    FoodRepository foodRepository;
    ProfileUserRepository profileUserRepository;
    FoodHistoryRepository foodHistoryRepository;
    //object mapper
    ObjectMapper objectMapper = new ObjectMapper();
    ProfileUserMapper profileUserMapper;
    PlanMapper planMapper;
    //another service
    FoodHistoryService foodHistoryService;
    EmailService emailService;
    @NonFinal
    @Value("${upload.dir}")
    String uploadDir;
    public Boolean imageToFood(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID().toString() + ".jpg";
        Path path = Paths.get(uploadDir, filename);
        file.transferTo(new File(path.toAbsolutePath().toString()));
        String imageUrl = "http://192.168.124.91:8080/app/upload/img/" + filename;

        //send imgUrl to AI
        long index = getIndexFromImage(imageUrl);
        if (index != 0){
            Food food = foodRepository.findFoodById(index).orElseThrow(() -> new AppException(ErrorCode.FOOD_NOTEXISTED));
            FoodHistory foodHistory = new FoodHistory();
            foodHistory.setFood(food);
            foodHistory.setCreateAt(new Date());
            foodHistory.setImageUrl(imageUrl);
            foodHistoryRepository.save(foodHistory);
            return true;
        }
        else{
            return false;
        }
    }
    private int getIndexFromImage(String imgUrl){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("url", imgUrl);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        String apiUrl = "http://192.168.124.254:8080/api/foodrecognition";
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

        try{
            // Parse the JSON response
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            return rootNode.get("food").asInt();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    public PlanningResponse getPlanning(){
        Optional<Plan> planOptional = planRepository.findPlanByUser_Email("binh@gmail.com");
        if (planOptional.isPresent()){
            return planMapper.toPlanningResponse(planOptional.get());
        }

        ProfileUser profileUser = profileUserRepository.findProfileUserByUser_Email("binh@gmail.com");
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        PlanningRequest body = profileUserMapper.toPlanningRequest(profileUser);
        HttpEntity<PlanningRequest> request = new HttpEntity<>(body, headers);

        String apiUrl = "http://192.168.124.254:8080/api/planning";
        ResponseEntity<PlanningResponse> response = restTemplate.postForEntity(apiUrl, request, PlanningResponse.class);

        try{
            // Parse the JSON response
            Plan plan = planMapper.toPlan(response.getBody());
            plan.setUser(profileUser.getUser());
            planRepository.save(plan);
            return response.getBody();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Boolean summarizeMeal(){
        Optional<Plan> planOptional = planRepository.findPlanByUser_Email("binh@gmail.com");
        if (planOptional.isEmpty())
            return null;
        Plan plan = planOptional.get();
        var realFoodHistory = foodHistoryService.getFoodHistoryByDate(
                Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant())
        );
        Plan real = Plan.builder()
                .calories(realFoodHistory.getTotalCalories())
                .fat(realFoodHistory.getTotalFat())
                .sugar(realFoodHistory.getTotalSugar())
                .protein(realFoodHistory.getTotalProtein())
                .fiber(realFoodHistory.getTotalFiber())
                .build();
        SummaryRequest summaryRequest = SummaryRequest.builder()
                .plan(plan)
                .real(real)
                .build();
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SummaryRequest> request = new HttpEntity<>(summaryRequest, headers);

        String apiUrl = "http://192.168.124.254:8080/api/compare";
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

        try{
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            String message = rootNode.path("message").asText();
            String html = convertMarkdownToHtml(message);
            return sendMail(html);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private boolean sendMail(String html) {
        List<String> recipients = new ArrayList<>();
        recipients.add("binh1920042018@gmail.com");
        EmailDTO emailDTO = EmailDTO.builder()
                .recipients(recipients)
                .body(html)
                .subject("Mã OTP đăng ký tài khoản.")
                .build();
        return emailService.sendSimpleMessage(emailDTO);
    }

    public String convertMarkdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
    public List<Food> getFoodSuggestion(){
        Plan plan = planRepository.findPlanByUser_Email("binh@gmail.com").orElseThrow(() -> new AppException(ErrorCode.PLAN_NOTEXISTED));
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Plan> request = new HttpEntity<>(plan, headers);

        String apiUrl = "http://192.168.124.254:8080/api/suggestion";
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
        try{
            JsonNode root = objectMapper.readTree(response.getBody());

// Lấy mảng các id food
            JsonNode foodArray = root.get("Foods");

            List<Food> foodList = new ArrayList<>();
            if (foodArray != null && foodArray.isArray()) {
                for (JsonNode idNode : foodArray) {
                    foodList.add(foodRepository.findFoodById(idNode.asLong()).orElseThrow(() -> new AppException(ErrorCode.FOOD_NOTEXISTED)));
                }
            }
            for (var food : foodList) {
                food.setImageUrl("http://192.168.124.91:8080/app/upload/img/20d85049-cf8d-4904-90af-219c4d6db4b4.jpg");
            }
            return foodList;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
