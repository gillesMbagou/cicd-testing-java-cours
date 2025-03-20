package tech.zerofiltre.testing.calcul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.zerofiltre.testing.calcul.domain.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    
    Boolean existsByUsername(String username);
}