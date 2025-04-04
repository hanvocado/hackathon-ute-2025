package hadup.server.server.controller;

import hadup.server.server.dto.ApiResponse;
import hadup.server.server.dto.request.AuthenticationRequest;
import hadup.server.server.dto.response.AuthenticationResponse;
import hadup.server.server.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        return ApiResponse.<AuthenticationResponse>builder()
//                .result(authenticationService.authenticate(authenticationRequest))
                .build();
    }
    @PostMapping("/introspect")
    public ApiResponse<Boolean> introspect(@RequestParam String token) {
        return ApiResponse.<Boolean>builder()
//                .result(authenticationService.introspect(token))
                .build();
    }
}
