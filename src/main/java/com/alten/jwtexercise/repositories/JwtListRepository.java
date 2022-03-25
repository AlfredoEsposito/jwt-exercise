package com.alten.jwtexercise.repositories;

import com.alten.jwtexercise.domain.JwtList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtListRepository extends JpaRepository<JwtList, Long> {

    Optional<JwtList> findByTokenEquals(String token);
}
