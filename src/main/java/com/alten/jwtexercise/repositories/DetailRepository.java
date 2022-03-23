package com.alten.jwtexercise.repositories;

import com.alten.jwtexercise.domain.Detail;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DetailRepository extends JpaRepository<Detail, Long> {

}
