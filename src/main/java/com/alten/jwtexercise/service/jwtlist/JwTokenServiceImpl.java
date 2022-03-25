package com.alten.jwtexercise.service.jwtlist;

import com.alten.jwtexercise.domain.JwToken;
import com.alten.jwtexercise.repositories.JwTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class JwTokenServiceImpl implements JwTokenService {

    private final JwTokenRepository jwTokenRepository;

    public JwTokenServiceImpl(JwTokenRepository jwTokenRepository) {
        this.jwTokenRepository = jwTokenRepository;
    }

    @Override
    public boolean tokenExists(String token){
        List<JwToken> tokens = jwTokenRepository.findAll();
        if(tokens.stream().anyMatch(t -> (t.getToken().equals(token)))){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public JwToken saveToken(String token) {
        JwToken jwt;
        if(token==null){
            log.error("Error! Missing token");
            throw new RuntimeException("Missing token jwt!");
        }else{
            jwt = new JwToken(token);
            jwTokenRepository.save(jwt);
            log.info("Jwt saved to database");
        }
        return jwt;
    }

    @Override
    public void deleteToken(String token) {
        Optional<JwToken> optionalJwt = jwTokenRepository.findByTokenEquals(token);
        JwToken jwt = null;
        if(optionalJwt.isPresent()){
            jwt = optionalJwt.get();
            jwTokenRepository.delete(jwt);
            log.info("Jwt deleted");
        }else{
            log.error("Error! Missing token");
            throw new RuntimeException("Missing token jwt!");
        }
    }
}
