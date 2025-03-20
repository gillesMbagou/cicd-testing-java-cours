package tech.zerofiltre.testing.calcul.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.zerofiltre.testing.calcul.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String newAccessToken = authService.refreshAccessToken(request.refreshToken());
        return ResponseEntity.ok(new AuthResponse(newAccessToken, request.refreshToken()));
    }

    public record LoginRequest(String username, String password) {}
    public record TokenRefreshRequest(String refreshToken) {}
    public record AuthResponse(String accessToken, String refreshToken) {}
}