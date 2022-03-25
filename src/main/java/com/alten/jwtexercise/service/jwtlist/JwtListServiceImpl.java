package com.alten.jwtexercise.service.jwtlist;

import com.alten.jwtexercise.domain.JwtList;
import com.alten.jwtexercise.repositories.JwtListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class JwtListServiceImpl implements JwtListService{

    private final JwtListRepository jwtListRepository;

    public JwtListServiceImpl(JwtListRepository jwtListRepository) {
        this.jwtListRepository = jwtListRepository;
    }

    @Override
    public boolean tokenExists(String token){
        List<JwtList> tokens = jwtListRepository.findAll();
        if(tokens.stream().anyMatch(t -> (t.getToken().equals(token)))){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public JwtList saveToken(String token) {
        JwtList jwt;
        if(token==null){
            log.error("Error! Missing token");
            throw new RuntimeException("Missing token jwt!");
        }else{
            jwt = new JwtList(token);
            jwtListRepository.save(jwt);
            log.info("Jwt saved to database");
        }
        return jwt;
    }

    @Override
    public void deleteToken(String token) {
        Optional<JwtList> optionalJwt = jwtListRepository.findByTokenEquals(token);
        JwtList jwt = null;
        if(optionalJwt.isPresent()){
            jwt = optionalJwt.get();
            jwtListRepository.delete(jwt);
            log.info("Jwt deleted");
        }else{
            log.error("Error! Missing token");
            throw new RuntimeException("Missing token jwt!");
        }
    }
}
