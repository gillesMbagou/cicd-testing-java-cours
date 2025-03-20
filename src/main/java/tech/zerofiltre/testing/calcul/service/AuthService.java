package tech.zerofiltre.testing.calcul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tech.zerofiltre.testing.calcul.controller.AuthController;
import tech.zerofiltre.testing.calcul.domain.model.RefreshToken;
import tech.zerofiltre.testing.calcul.exception.TokenRefreshException;
import tech.zerofiltre.testing.calcul.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthController.AuthResponse authenticate(AuthController.LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
            )
        );
        
        UserDetails user = userDetailsService.loadUserByUsername(request.username());
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user.getUsername()).getToken();
        
        return new AuthController.AuthResponse(accessToken, refreshToken);
    }

    public String refreshAccessToken(String refreshToken) {
        RefreshToken token = refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new TokenRefreshException("Refresh token invalide"));

        refreshTokenService.verifyExpiration(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(token.getUser().getUsername());

        return jwtUtil.generateAccessToken(userDetails);
    }
}