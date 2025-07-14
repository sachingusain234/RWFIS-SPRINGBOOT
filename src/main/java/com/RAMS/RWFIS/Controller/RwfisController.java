package com.RAMS.RWFIS.Controller;

import com.RAMS.RWFIS.Entity.RwfisEntity;
import com.RAMS.RWFIS.Service.RwfisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api")
public class RwfisController {
    @Autowired
    private RwfisService rwfisService;

    @GetMapping("")
    public List<RwfisEntity> getAll() {
        return rwfisService.getAll()
                .stream()
                .filter(entity -> !entity.isDeleted())
                .collect(Collectors.toList());
    }
    @GetMapping("/deleted")
    public List<RwfisEntity> getAllDeleted() {
        return rwfisService.getAll()
                .stream()
                .filter(entity -> entity.isDeleted())
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        RwfisEntity curr = rwfisService.getById(id);

        if (curr == null) {
            return ResponseEntity.notFound().build();
        }

        if (!curr.isDeleted()) {
            return ResponseEntity.ok(curr);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Entity is marked as deleted.");
        }
    }


    @PostMapping
    public ResponseEntity<RwfisEntity> create(@Valid @RequestBody RwfisEntity rwfis) {
        return ResponseEntity.ok(rwfisService.create(rwfis));
    }
    @PutMapping("/{id}")
    public ResponseEntity<RwfisEntity> update(@PathVariable Long id, @Valid @RequestBody RwfisEntity updated) {
        return ResponseEntity.ok(rwfisService.update(id, updated));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        RwfisEntity curr = rwfisService.getById(id);
        curr.setDeleted(true);
        rwfisService.create(curr);
        return ResponseEntity.ok("Resource with id " + id + " deleted successfully.");
    }

}
