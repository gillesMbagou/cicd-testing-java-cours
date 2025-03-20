package tech.zerofiltre.testing.calcul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import tech.zerofiltre.testing.calcul.domain.model.RefreshToken;
import tech.zerofiltre.testing.calcul.domain.model.User;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    
    @Modifying
    void deleteByUser(User user);
}