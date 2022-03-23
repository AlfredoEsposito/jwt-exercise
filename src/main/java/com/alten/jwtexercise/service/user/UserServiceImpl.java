package com.alten.jwtexercise.service.user;

import com.alten.jwtexercise.domain.Role;
import com.alten.jwtexercise.domain.User;
import com.alten.jwtexercise.enums.Roles;
import com.alten.jwtexercise.repositories.RoleRepository;
import com.alten.jwtexercise.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Utility method : check if username already exists
    public boolean checkUsername(User user){
       List<User> users = userRepository.findAll();
       if(users.stream().anyMatch(u -> (u.getUsername().equals(user.getUsername())))){
           return true;
       }else{
           return false;
       }
    }

    //Utility method : check if role already exists
    public boolean checkRole(Role role){
        List<Role> roles = roleRepository.findAll();
        if(roles.stream().anyMatch(r -> (r.getRoleName().name().equals(role.getRoleName().name())))){
            return true;
        }else{
            return false;
        }
    }


    @Override
    public User saveUser(User user) {
        log.info("Saving '{}' as new user to database...", user.getUsername());
        if(checkUsername(user)){
            log.error("Error! Username already in use. Choose another one");
            throw new RuntimeException("Username "+user.getUsername()+" already exists");
        }else{
            user.setId(0L);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role '{}' to database...", role.getRoleName());
       if(checkRole(role)){
           log.error("Error! Role already exist");
           throw new RuntimeException("Role "+role.getRoleName()+" already exists");
       }else{
           role.setId(0L);
           return roleRepository.save(role);
       }
    }

    @Override
    public User getUserById(Long id) {
        log.info("Getting user with id '{}' from database...", id);
        Optional<User> optionalUser = userRepository.findById(id);
        User user = null;
        if(optionalUser.isPresent()){
            user = optionalUser.get();
        }
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        log.info("Getting user '{}' from database...", username);
        Optional<User> optionalUser = userRepository.findByUsername(username);
        User user = null;
        if(optionalUser.isPresent()){
            user = optionalUser.get();
        }
        return user;
    }

    @Override
    public Set<User> getUsers() {
        log.info("Getting all users from database...");
        return userRepository.findAll().stream().collect(Collectors.toSet());
    }

    @Override
    public User updateUser(User user) {
        log.info("Updating user '{}' ...", user.getUsername());
        if(checkUsername(user)){
            log.error("Error! Username already in use. Choose another one");
            throw new RuntimeException("Username '"+user.getUsername()+"' already exists");
        }else{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.saveAndFlush(user);
        }
    }

    @Override
    public void deleteUserById(Long id) {
        log.info("Deleting user with id '{}' from database...", id);
        userRepository.deleteById(id);
    }

    @Override
    public void addRoleToUser(String roleName, String username) {
        Optional<Role> optionalRole = roleRepository.findByRoleName(Roles.valueOf(roleName));
        Optional<User> optionalUser = userRepository.findByUsername(username);
        Role role;
        User user;
        if(optionalRole.isPresent() && optionalUser.isPresent()){
            log.info("Adding role {} to user '{}'", roleName, username);
            role = optionalRole.get();
            user = optionalUser.get();
            if(user.getRoles().contains(role)){
                log.error("Error! Role already present");
                throw new RuntimeException("User '"+username+"' already has role '"+roleName+"'");
            }else{
                user.getRoles().add(role);
            }
        }else{
            log.error("Error! User or Role not found in the database");
            throw new RuntimeException("User or Role doesn't exists");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).get();
        if(user==null){
            log.error("User not found in the database");
            throw new UsernameNotFoundException("Username '"+username+"' doesn't exists");
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName().name()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
