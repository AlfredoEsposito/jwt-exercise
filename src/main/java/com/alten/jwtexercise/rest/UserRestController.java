package com.alten.jwtexercise.rest;

import com.alten.jwtexercise.domain.Role;
import com.alten.jwtexercise.domain.User;
import com.alten.jwtexercise.exception.MyCustomException;
import com.alten.jwtexercise.service.user.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MKT', 'ROLE_ICM', 'ROLE_CP')")
    public ResponseEntity<Set<User>>getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MKT', 'ROLE_ICM', 'ROLE_CP')")
    public ResponseEntity<User>getUserById(@PathVariable Long userId){
        User user = userService.getUserById(userId);
        if(user==null){
            throw new MyCustomException("User with id '"+userId+"' not found!");
        }
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/users/retrieveByUsername/{username}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MKT', 'ROLE_ICM', 'ROLE_CP')")
    public ResponseEntity<User>getUserByUsername(@PathVariable String username){
        User user = userService.getUserByUsername(username);
        if(user==null){
            throw new MyCustomException("User with username '"+username+"' not found!");
        }
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User>saveUser(@RequestBody User user){
        return ResponseEntity.ok().body(userService.saveUser(user));
    }

    @PostMapping("/roles")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Role>saveRole(@RequestBody Role role){
        return ResponseEntity.ok().body(userService.saveRole(role));
    }

    @PostMapping("/users/addRole/{roleName}/ToUser/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String>addRoleToUser(@PathVariable String roleName, @PathVariable String username){
        userService.addRoleToUser(roleName, username);
        return ResponseEntity.ok().body("Role '"+roleName+"' added to User '"+username+"'");
    }

    @PutMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User>updateUser(@RequestBody User user){
        return ResponseEntity.ok().body(userService.updateUser(user));
    }

    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String>deleteUser(@PathVariable Long userId){
        User user = userService.getUserById(userId);
        if(user==null){
            throw new MyCustomException("User with id '"+userId+"' not found!");
        }
        userService.deleteUserById(userId);
        return ResponseEntity.ok().body("User with id '"+userId+"' deleted");
    }

}
