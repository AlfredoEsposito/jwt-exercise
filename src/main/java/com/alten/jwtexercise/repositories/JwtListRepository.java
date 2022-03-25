package com.alten.jwtexercise.repositories;

import com.alten.jwtexercise.domain.JwToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtListRepository extends JpaRepository<JwToken, Long> {

    Optional<JwToken> findByTokenEquals(String token);
}
