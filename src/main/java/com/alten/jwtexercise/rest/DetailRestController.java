package com.alten.jwtexercise.rest;

import com.alten.jwtexercise.domain.Detail;
import com.alten.jwtexercise.exception.MyCustomException;
import com.alten.jwtexercise.service.detail.DetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class DetailRestController {

    private final DetailService detailService;

    @Autowired
    public DetailRestController(DetailService detailService) {
        this.detailService = detailService;
    }

    @GetMapping("/details")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CP')")
    public ResponseEntity<Set<Detail>> getDetails(){
        return ResponseEntity.ok().body(detailService.getDetails());
    }

    @GetMapping("/details/{detailId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CP')")
    public ResponseEntity<Detail> getDetailById(@PathVariable Long detailId){
        Detail detail = detailService.getDetailById(detailId);
        if(detail==null){
            throw new MyCustomException("Detail with id '"+detailId+"' not found!");
        }
        return ResponseEntity.ok().body(detail);
    }

    @PostMapping("/details")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CP')")
    public ResponseEntity<Detail> saveDetail(@RequestBody Detail detail){
        return ResponseEntity.ok().body(detailService.saveDetail(detail));
    }

    @PutMapping("/details")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CP')")
    public ResponseEntity<Detail> updateDetail(@RequestBody Detail detail){
        return ResponseEntity.ok().body(detailService.updateDetail(detail));
    }

    @DeleteMapping("/details/{detailId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CP')")
    public ResponseEntity<String> deleteDetail(@PathVariable Long detailId){
        Detail detail = detailService.getDetailById(detailId);
        if(detail==null){
            throw new MyCustomException("Detail with id '"+detailId+"' not found!");
        }
        detailService.deleteDetailById(detailId);
        return ResponseEntity.ok().body("Detail with id '"+detailId+"' deleted");
    }
}
