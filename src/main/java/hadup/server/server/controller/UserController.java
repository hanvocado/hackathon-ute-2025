package hadup.server.server.controller;

import hadup.server.server.dto.ApiResponse;
import hadup.server.server.dto.request.UserRegisterRequest;
import hadup.server.server.entity.User;
import hadup.server.server.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        return ApiResponse.<User>builder()
                .result(userService.register(userRegisterRequest))
                .build();
    }
}
