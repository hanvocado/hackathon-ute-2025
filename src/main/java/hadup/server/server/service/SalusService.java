package hadup.server.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hadup.server.server.dto.request.PlanningRequest;
import hadup.server.server.dto.response.PlanningResponse;
import hadup.server.server.entity.Food;
import hadup.server.server.entity.FoodHistory;
import hadup.server.server.entity.ProfileUser;
import hadup.server.server.entity.User;
import hadup.server.server.exception.AppException;
import hadup.server.server.exception.ErrorCode;
import hadup.server.server.mapper.ProfileUserMapper;
import hadup.server.server.repository.FoodHistoryRepository;
import hadup.server.server.repository.FoodRepository;
import hadup.server.server.repository.ProfileUserRepository;
import hadup.server.server.repository.UserRepository;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SalusService {
    FoodRepository foodRepository;
    ProfileUserRepository profileUserRepository;
    FoodHistoryRepository foodHistoryRepository;
    //object mapper
    ObjectMapper objectMapper = new ObjectMapper();
    ProfileUserMapper userMapper;
    private final ProfileUserMapper profileUserMapper;
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
            foodHistory.setCreateAt(LocalDate.now());
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
            return response.getBody();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
