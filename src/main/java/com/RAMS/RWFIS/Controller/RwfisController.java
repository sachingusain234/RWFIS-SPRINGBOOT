package com.RAMS.RWFIS.Controller;

import com.RAMS.RWFIS.Entity.RwfisEntity;
import com.RAMS.RWFIS.Service.RwfisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RwfisController {

    @Autowired
    private RwfisService rwfisService;

    @GetMapping("")
    public List<RwfisEntity> getAll() {
        return rwfisService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RwfisEntity> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rwfisService.getById(id));
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
        rwfisService.delete(id);
        return ResponseEntity.ok("Resource with id " + id + " deleted successfully.");
    }

}
