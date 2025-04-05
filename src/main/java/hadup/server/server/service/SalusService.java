package hadup.server.server.service;

import hadup.server.server.dto.response.FoodImageResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SalusService {

    @NonFinal
    @Value("${upload.dir}")
    String uploadDir;
    public FoodImageResponse imageToFood(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID().toString() + ".jpg";
        Path path = Paths.get(uploadDir, filename);
        file.transferTo(new File(path.toAbsolutePath().toString()));
        String imageUrl = "/app/upload/img/" + filename;
        return FoodImageResponse.builder()
                .urlImage(imageUrl)
                .success(true)
                .build();
    }
}
