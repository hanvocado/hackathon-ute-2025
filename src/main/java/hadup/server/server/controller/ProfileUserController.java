package hadup.server.server.controller;

import hadup.server.server.dto.ApiResponse;
import hadup.server.server.dto.request.ProfileRequest;
import hadup.server.server.dto.request.UserRegisterRequest;
import hadup.server.server.entity.User;
import hadup.server.server.service.ProfileUserService;
import hadup.server.server.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileUserController {
    ProfileUserService profileUserService;

    @PostMapping("/init")
    public ApiResponse<Boolean> register(@RequestBody ProfileRequest profileRequest) {
        return ApiResponse.<Boolean>builder()
                .result(profileUserService.init(profileRequest))
                .build();
    }
}
