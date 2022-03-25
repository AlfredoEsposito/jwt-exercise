package com.alten.jwtexercise.service.jwtlist;

import com.alten.jwtexercise.domain.JwToken;

public interface JwTokenService {

    JwToken saveToken(String token);
    void deleteToken(String token);
    boolean tokenExists(String token);
}
