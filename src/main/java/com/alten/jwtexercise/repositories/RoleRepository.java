package com.alten.jwtexercise.repositories;

import com.alten.jwtexercise.domain.Role;
import com.alten.jwtexercise.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(Roles roleName);
}
