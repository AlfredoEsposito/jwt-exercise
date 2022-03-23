package com.alten.jwtexercise.service.detail;

import com.alten.jwtexercise.domain.Detail;

import java.util.Set;

public interface DetailService {

    Detail saveDetail(Detail detail);
    Detail getDetailById(Long id);
    Set<Detail> getDetails();
    Detail updateDetail(Detail detail);
    void deleteDetailById(Long id);
}
