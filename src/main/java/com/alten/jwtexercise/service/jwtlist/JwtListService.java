package com.alten.jwtexercise.service.jwtlist;

import com.alten.jwtexercise.domain.JwtList;

public interface JwtListService {

    JwtList saveToken(String token);
    void deleteToken(String token);
    boolean tokenExists(String token);
}
