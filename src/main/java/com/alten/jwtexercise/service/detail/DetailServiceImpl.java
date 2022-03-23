package com.alten.jwtexercise.service.detail;

import com.alten.jwtexercise.domain.Detail;
import com.alten.jwtexercise.repositories.DetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class DetailServiceImpl implements DetailService{

    private final DetailRepository detailRepository;

    @Autowired
    public DetailServiceImpl(DetailRepository detailRepository) {
        this.detailRepository = detailRepository;
    }

    @Override
    public Detail saveDetail(Detail detail) {
        log.info("Saving detail to database...");
        detail.setId(0L);
        return detailRepository.save(detail);
    }

    @Override
    public Detail getDetailById(Long id) {
        log.info("Getting detail with id '{}' from database...",id);
        Optional<Detail> optionalDetail = detailRepository.findById(id);
        Detail detail = null;
        if(optionalDetail.isPresent()){
            detail=optionalDetail.get();
        }
        return detail;
    }

    @Override
    public Set<Detail> getDetails() {
        log.info("Getting all details from database...");
        return detailRepository.findAll().stream().collect(Collectors.toSet());
    }

    @Override
    public Detail updateDetail(Detail detail) {
        log.info("Updating detail...");
        return detailRepository.saveAndFlush(detail);
    }

    @Override
    public void deleteDetailById(Long id) {
        log.info("Deleting detail with id '{}' from database...", id);
        detailRepository.deleteById(id);
    }

}
