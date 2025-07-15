package com.RAMS.RWFIS.Controller;

import com.RAMS.RWFIS.DTO.RwfisDTO;
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
    public List<RwfisDTO> getAll() {
        return rwfisService.getAll()
                .stream()
                .filter(entity -> !entity.isDeleted())
                .map(entity -> rwfisService.mapToDTO(entity))  // convert entity to DTO here
                .collect(Collectors.toList());
    }

    @GetMapping("/deleted")
    public List<RwfisDTO> getAllDeleted() {
        return rwfisService.getAll()
                .stream()
                .filter(entity -> entity.isDeleted())
                .map(entity -> rwfisService.mapToDTO(entity))  // convert entity to DTO here
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


    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody RwfisEntity rwfis) {
        return ResponseEntity.ok(rwfisService.create(rwfis));
    }
    @PutMapping("/{id}/updatedBy/{nid}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody RwfisEntity updated,@PathVariable Long nid) {
        return ResponseEntity.ok(rwfisService.update(id, updated,nid));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        RwfisEntity curr = rwfisService.getById(id);
        curr.setDeleted(true);
        rwfisService.create(curr);
        return ResponseEntity.ok("Resource with id " + id + " deleted successfully.");
    }

}
