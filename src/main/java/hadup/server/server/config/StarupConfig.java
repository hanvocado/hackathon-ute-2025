package hadup.server.server.config;


import hadup.server.server.utils.FileStorageUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StarupConfig {
    @Value("${upload.dir}") // Lấy đường dẫn từ application.properties
    private String uploadDir;

    @PostConstruct
    public void init() {
        FileStorageUtil.createFolderIfNotExists(uploadDir);
    }
}
