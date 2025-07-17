package com.RAMS.RWFIS.Controller;

import com.RAMS.RWFIS.DTO.RsiDTO;
import com.RAMS.RWFIS.DTO.RwfisDTO;
import com.RAMS.RWFIS.Entity.RwfisEntity;
import com.RAMS.RWFIS.Service.RwfisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}

/**
 * Standardized API response class for consistent success/error messages.
 */
class ApiResponse {
    private int status;
    private String message;

    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
@RestController
@RequestMapping("/api")
public class RwfisController {
    private static final Logger log = LoggerFactory.getLogger(RwfisController.class);
    @Autowired
    private RwfisService rwfisService;

    @GetMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<Map<String, Object>> getAll() {
        log.info("Inside getAll method: Fetching all non-deleted Rwfis entities.");
        Map<String, Object> responseMap = new LinkedHashMap<>();
        List<RwfisDTO> responseList = new ArrayList<>();
        try {
            responseList = rwfisService.getAll()
                    .stream()
                    .filter(entity -> !entity.isDeleted())
                    .map(rwfisService::mapToDTO) // convert entity to DTO
                    .collect(Collectors.toList());

            responseMap.put("status", "success");
            responseMap.put("data", responseList);
            log.info("Successfully retrieved {} non-deleted Rwfis entities.", responseList.size());
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        } catch (Exception e) {
            // Log unexpected errors
            log.error("Error occurred while fetching all non-deleted Rwfis entities: {}", e.getMessage(), e);
            responseMap.put("status", "error");
            responseMap.put("message", "An unexpected error occurred while retrieving data. Please try again later.");
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/deleted", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<Map<String, Object>> getAllDeleted() {
        log.info("Inside getAllDeleted method: Fetching all deleted Rwfis entities.");
        Map<String, Object> responseMap = new LinkedHashMap<>();
        List<RwfisDTO> responseList = new ArrayList<>();
        try {
            responseList = rwfisService.getAll()
                    .stream()
                    .filter(RwfisEntity::isDeleted) // filter for deleted entities
                    .map(rwfisService::mapToDTO) // convert entity to DTO
                    .collect(Collectors.toList());

            responseMap.put("status", "success");
            responseMap.put("data", responseList);
            log.info("Successfully retrieved {} deleted Rwfis entities.", responseList.size());
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        } catch (Exception e) {
            // Log unexpected errors
            log.error("Error occurred while fetching all deleted Rwfis entities: {}", e.getMessage(), e);
            responseMap.put("status", "error");
            responseMap.put("message", "An unexpected error occurred while retrieving deleted data. Please try again later.");
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        log.info("Inside getById method with ID: {}", id);
        Map<String, Object> responseMap = new LinkedHashMap<>();
        try {
            RwfisEntity curr = rwfisService.getById(id);

            if (curr == null) {
                log.warn("Rwfis entity not found for ID: {}", id);
                responseMap.put("status", "error");
                responseMap.put("message", "Rwfis entity not found with ID: " + id);
                return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
            }

            if (curr.isDeleted()) {
                log.warn("Attempted to retrieve deleted Rwfis entity with ID: {}", id);
                responseMap.put("status", "error");
                responseMap.put("message", "Entity with ID " + id + " is marked as deleted.");
                return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND); // Using NOT_FOUND as per memorized pattern for resource not available
            }

            responseMap.put("status", "success");
            responseMap.put("data", curr);
            log.info("Successfully retrieved Rwfis entity with ID: {}", id);
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        } catch (Exception e) {
            // Log unexpected errors
            log.error("Error occurred while fetching Rwfis entity with ID {}: {}", id, e.getMessage(), e);
            responseMap.put("status", "error");
            responseMap.put("message", "An unexpected error occurred while retrieving the entity. Please try again later.");
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value = "", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<Object> create(@Valid @RequestBody RwfisEntity rwfis) {
        log.info("Request to create new Rwfis entity.");
        try {
            // Corrected: The service method returns RwfisDTO, so the variable should be RwfisDTO
            RwfisDTO createdDto = rwfisService.create(rwfis);
            ApiResponse response = new ApiResponse(1, "Rwfis entity created successfully!");
            // Log using the ID from the DTO
            log.info("Rwfis entity created successfully with ID: {}", createdDto.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CustomException e) {
            // Log custom exceptions
            log.error("Error creating Rwfis entity: {}", e.getMessage());
            return new ResponseEntity<>(createErrorResponse(e, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Log unexpected errors
            log.error("Unexpected error occurred while creating Rwfis entity: {}", e.getMessage(), e);
            return new ResponseEntity<>(createErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping(value = "/{id}/updatedBy/{nid}", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody RwfisEntity updated, @PathVariable Long nid) {
        log.info("Request to update Rwfis entity with ID: {}, updated by NID: {}", id, nid);
        try {
            // Corrected: The service method returns RwfisDTO, so the variable should be RwfisDTO
            RwfisDTO updatedDto = rwfisService.update(id, updated, nid);
            ApiResponse response = new ApiResponse(1, "Rwfis entity updated successfully!");
            // Log using the ID from the DTO
            log.info("Rwfis entity with ID {} updated successfully.", updatedDto.getId());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (CustomException e) {
            // Log custom exceptions
            log.error("Error updating Rwfis entity with ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(createErrorResponse(e, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Log unexpected errors
            log.error("Unexpected error occurred while updating Rwfis entity with ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(createErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        log.info("Request to mark Rwfis entity with ID {} as deleted.", id);
        Map<String, Object> responseMap = new LinkedHashMap<>();
        try {
            RwfisEntity curr = rwfisService.getById(id);
            if (curr == null) {
                log.warn("Attempted to delete non-existent Rwfis entity with ID: {}", id);
                responseMap.put("status", "error");
                responseMap.put("message", "Rwfis entity not found with ID: " + id);
                return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
            }

            curr.setDeleted(true);
            rwfisService.create(curr); // Assuming create also handles updates if entity exists
            responseMap.put("status", "success");
            responseMap.put("message", "Resource with id " + id + " deleted successfully.");
            log.info("Rwfis entity with ID {} marked as deleted successfully.", id);
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        } catch (CustomException e) {
            // Log custom exceptions
            log.error("Error marking Rwfis entity with ID {} as deleted: {}", id, e.getMessage());
            responseMap.put("status", "error");
            responseMap.put("message", e.getMessage());
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Log unexpected errors
            log.error("Unexpected error occurred while marking Rwfis entity with ID {} as deleted: {}", id, e.getMessage(), e);
            responseMap.put("status", "error");
            responseMap.put("message", "An unexpected error occurred during deletion. Please try again later.");
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/rsi-id/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<Map<String, Object>> getAllRsiID(@PathVariable Long id) {
        log.info("Inside getAllRsiID method with RSI ID: {}", id);
        Map<String, Object> responseMap = new LinkedHashMap<>();
        try {
            RwfisEntity entity = rwfisService.getByRsiId(id);

            if (entity == null) {
                log.warn("Rwfis entity not found for RSI ID: {}", id);
                responseMap.put("status", "error");
                responseMap.put("message", "Rwfis entity not found with RSI ID: " + id);
                return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
            }

            if (entity.isDeleted()) {
                log.warn("Attempted to retrieve deleted Rwfis entity with RSI ID: {}", id);
                responseMap.put("status", "error");
                responseMap.put("message", "The record with rsi_id " + id + " has been deleted.");
                return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND); // Using NOT_FOUND as per memorized pattern
            }

            RsiDTO dto = rwfisService.mapToDto(entity);
            responseMap.put("status", "success");
            responseMap.put("data", dto);
            log.info("Successfully retrieved Rwfis entity with RSI ID: {}", id);
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        } catch (Exception e) {
            // Log unexpected errors
            log.error("Error occurred while fetching Rwfis entity with RSI ID {}: {}", id, e.getMessage(), e);
            responseMap.put("status", "error");
            responseMap.put("message", "An unexpected error occurred while retrieving the entity by RSI ID. Please try again later.");
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/rsi-id/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<Map<String, Object>> deleteRsi(@PathVariable Long id) {
        log.info("Request to mark Rwfis entity with RSI ID {} as deleted.", id);
        Map<String, Object> responseMap = new LinkedHashMap<>();
        try {
            RwfisEntity curr = rwfisService.getByRsiId(id);
            if (curr == null) {
                log.warn("Attempted to delete non-existent Rwfis entity with RSI ID: {}", id);
                responseMap.put("status", "error");
                responseMap.put("message", "Rwfis entity not found with RSI ID: " + id);
                return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
            }

            curr.setDeleted(true);
            rwfisService.create(curr); // Assuming create also handles updates if entity exists
            responseMap.put("status", "success");
            responseMap.put("message", "Resource with rsi_id " + id + " deleted successfully.");
            log.info("Rwfis entity with RSI ID {} marked as deleted successfully.", id);
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        } catch (CustomException e) {
            // Log custom exceptions
            log.error("Error marking Rwfis entity with RSI ID {} as deleted: {}", id, e.getMessage());
            responseMap.put("status", "error");
            responseMap.put("message", e.getMessage());
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Log unexpected errors
            log.error("Unexpected error occurred while marking Rwfis entity with RSI ID {} as deleted: {}", id, e.getMessage(), e);
            responseMap.put("status", "error");
            responseMap.put("message", "An unexpected error occurred during deletion by RSI ID. Please try again later.");
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private Map<String, Object> createErrorResponse(Exception e, HttpStatus status) {
        Map<String, Object> responseBody = new LinkedHashMap<>(); // Using LinkedHashMap for consistent order
        responseBody.put("status", status.value());
        responseBody.put("error", status.getReasonPhrase());
        responseBody.put("message", e.getMessage());
        return responseBody;
    }
}
