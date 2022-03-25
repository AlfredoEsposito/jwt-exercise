package com.alten.jwtexercise.rest;

import com.alten.jwtexercise.service.jwtlist.JwtListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LogoutRestController {

    private final JwtListService jwtListService;

    public LogoutRestController(JwtListService jwtListService) {
        this.jwtListService = jwtListService;
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token){
        String jwt = token.substring("Bearer ".length());
        jwtListService.deleteToken(jwt);
        return ResponseEntity.ok().body("Logout successful!");
    }
}
