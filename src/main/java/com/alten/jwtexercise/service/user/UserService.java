package com.alten.jwtexercise.service.user;

import com.alten.jwtexercise.domain.Role;
import com.alten.jwtexercise.domain.User;
import com.alten.jwtexercise.enums.Roles;

import java.util.Set;

public interface UserService {

    User saveUser(User user);
    User getUserById(Long id);
    User getUserByUsername(String username);
    Set<User> getUsers();
    User updateUser(User user);
    void deleteUserById(Long id);
    void addRoleToUser(String roleName, String username);
    Role saveRole(Role role);

}
