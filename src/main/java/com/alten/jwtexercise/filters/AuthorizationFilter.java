package com.alten.jwtexercise.filters;

import com.alten.jwtexercise.jwt.JwtConfig;
import com.alten.jwtexercise.service.jwtlist.JwTokenService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final JwTokenService jwTokenService;

    @Autowired
    public AuthorizationFilter(JwtConfig jwtConfig, JwTokenService jwTokenService) {
        this.jwtConfig = jwtConfig;
        this.jwTokenService = jwTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/authn")){
            filterChain.doFilter(request, response);
        }else{
            String tokenJwt = null;
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
                 tokenJwt = authorizationHeader.substring("Bearer ".length());
                if(jwTokenService.tokenExists(tokenJwt)) {
                    try {
                        //Verifica token
                        log.info("Verifying token...");
                        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey().getBytes());
                        JWTVerifier verifier = JWT.require(algorithm).build();
                        DecodedJWT decodedJWT = verifier.verify(tokenJwt);
                        log.info("Token verified");

                        //recupero parti del token
                        log.info("Retrieving parts of the verified token...");
                        String username = decodedJWT.getSubject();
                        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
                        stream(roles).forEach(role -> {
                            authorities.add(new SimpleGrantedAuthority(role));
                        });

                        //autenticazione il token
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        log.info("Token authenticated");

                        filterChain.doFilter(request, response);
                    } catch (Exception exception) {
                        log.error("Error: {}" + exception.getMessage());
                        response.setHeader("error", exception.getMessage());
                        response.setStatus(FORBIDDEN.value());
                        Map<String, String> errors = new HashMap<>();
                        errors.put("error_message", exception.getMessage());
                        response.setContentType(APPLICATION_JSON_VALUE);
                        new ObjectMapper().writeValue(response.getOutputStream(), errors);
                    }
                }else{
                    log.error("Error! Token unavailable or not found");
                    response.setHeader("error", "Unauthorized! Token unavailable or not found");
                    response.setStatus(UNAUTHORIZED.value());
                    Map<String, String> errors = new HashMap<>();
                    errors.put("error_message", "Unauthorized! Token unavailable or not found");
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), errors);
                }
            }else{
                log.error("Error! Invalid authorization header");
                response.setHeader("error", "Invalid authorization header");
                response.setStatus(FORBIDDEN.value());
                Map<String, String> errors = new HashMap<>();
                errors.put("error_message", "Invalid authorization header");
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
            }
        }
    }

}
