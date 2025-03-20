package tech.zerofiltre.testing.calcul.interceptor;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import tech.zerofiltre.testing.calcul.util.JwtUtil;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // Autoriser les requêtes OPTIONS pour le CORS
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // Extraire le token du header
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendError(response, "Token manquant");
            return false;
        }

        // Valider le token
        String jwt = authHeader.substring(7);
        try {
            if (!jwtUtil.validateToken(jwt)) {
                sendError(response, "Token invalide");
                return false;
            }

            // Vérifier l'expiration spécifiquement
            if (jwtUtil.isTokenExpired(jwt)) {
                sendError(response, "Token expiré");
                return false;
            }

            // Ajouter le nom d'utilisateur à la requête
            request.setAttribute("username", jwtUtil.extractUsername(jwt));
            return true;

        } catch (JwtException e) {
            sendError(response, "Erreur de validation JWT: " + e.getMessage());
            return false;
        }
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{ \"error\": \"" + message + "\" }");
    }
}