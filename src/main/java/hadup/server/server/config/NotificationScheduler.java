package hadup.server.server.config;

import hadup.server.server.service.SalusService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationScheduler {
    SalusService salusService;
    @Scheduled(cron = "0 23 6 * * *")
    public void sendDailyNotification() {
        salusService.summarizeMeal();
    }
}
