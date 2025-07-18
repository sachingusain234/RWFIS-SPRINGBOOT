package com.RAMS.RWFIS.Service;

import com.RAMS.RWFIS.DTO.RsiDTO;
import com.RAMS.RWFIS.DTO.RwfisDTO;
import com.RAMS.RWFIS.Entity.RwfisEntity;
import com.RAMS.RWFIS.Repository.RwfisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RwfisService {
    private static final Logger log = LoggerFactory.getLogger(RwfisService.class);
    @Autowired
    private RwfisRepository repository;
    public List<RwfisEntity> getAll() {
        log.info("Attempting to retrieve all Rwfis entities from the repository.");
        try {
            List<RwfisEntity> entities = repository.findAll();
            log.info("Successfully retrieved {} Rwfis entities.", entities.size());
            return entities;
        } catch (Exception e) {
            // Log the error detailed in the service layer
            log.error("Error occurred while fetching all Rwfis entities: {}", e.getMessage(), e);
            // Re-throw a generic RuntimeException. The controller's catch(Exception e)
            // will then handle this and return a 500 INTERNAL_SERVER_ERROR,
            // which aligns with unexpected errors originating from the service.
            throw new RuntimeException("Failed to retrieve all Rwfis entities: " + e.getMessage(), e);
        }
    }
    public RwfisEntity getByRsiId(Long id) {
        log.info("Attempting to retrieve Rwfis entity by RSI ID: {}", id);
        try {
            // Using Optional.orElse(null) to return null if not found,
            // which aligns with how your controller handles "not found" for getById and getAllRsiID.
            Optional<RwfisEntity> entityOptional = repository.findByRsiId(id);

            if (entityOptional.isPresent()) {
                RwfisEntity entity = entityOptional.get();
                log.info("Successfully retrieved Rwfis entity with RSI ID: {}", id);
                return entity;
            } else {
                log.warn("Rwfis entity not found for RSI ID: {}", id);
                return null; // Return null for not found, as handled by your controller
            }
        } catch (Exception e) {
            // Log the error detailed in the service layer
            log.error("Error occurred while fetching Rwfis entity by RSI ID {}: {}", id, e.getMessage(), e);
            // Re-throw a generic RuntimeException for unexpected errors,
            // which the controller's catch(Exception e) will handle.
            throw new RuntimeException("Failed to retrieve Rwfis entity by RSI ID " + id + ": " + e.getMessage(), e);
        }
    }

    public RwfisEntity getById(Long id) {
        log.info("Attempting to retrieve Rwfis entity by ID: {}", id);
        try {
            // Using Optional.orElse(null) to return null if not found.
            // This aligns with how your controller's getById method handles "not found".
            Optional<RwfisEntity> entityOptional = repository.findById(id);

            if (entityOptional.isPresent()) {
                RwfisEntity entity = entityOptional.get();
                log.info("Successfully retrieved Rwfis entity with ID: {}", id);
                return entity;
            } else {
                log.warn("Rwfis entity not found for ID: {}", id);
                return null; // Return null for not found, as handled by your controller
            }
        } catch (Exception e) {
            // Log the error detailed in the service layer
            log.error("Error occurred while fetching Rwfis entity by ID {}: {}", id, e.getMessage(), e);
            // Re-throw a generic RuntimeException for unexpected errors,
            // which the controller's catch(Exception e) will handle.
            throw new RuntimeException("Failed to retrieve Rwfis entity by ID " + id + ": " + e.getMessage(), e);
        }
    }
    @Transactional
    public RwfisDTO create(RwfisEntity rwfis) {
        log.info("Attempting to create new Rwfis entity.");
        try {
            // First save to generate the ID
            RwfisEntity curr = repository.save(rwfis);
            log.info("Initial save of Rwfis entity completed with ID: {}", curr.getId());

            // Check if createdBy needs to be set to its own ID
            if (curr.getCreatedBy() == null) {
                log.warn("createdBy field is null for new Rwfis entity with ID {}, setting it to entity ID.", curr.getId());
                curr.setCreatedBy(curr.getId());
                // Second save to update the createdBy field
                curr = repository.save(curr);
                log.info("Updated Rwfis entity with ID {} to set createdBy field.", curr.getId());
            }

            RwfisDTO createdDto = mapToDTO(curr);
            log.info("Rwfis entity created successfully and mapped to DTO for ID: {}", createdDto.getId());
            return createdDto;
        } catch (Exception e) {
            // Log the error detailed in the service layer
            log.error("Error occurred while creating Rwfis entity: {}", e.getMessage(), e);
            // Re-throw a generic RuntimeException for unexpected errors,
            // which the controller's catch(Exception e) will handle.
            throw new RuntimeException("Failed to create Rwfis entity: " + e.getMessage(), e);
        }
    }
    public RwfisDTO mapToDTO(RwfisEntity entity) {
        // Adding a null check for robustness, though typically the entity should not be null here.
        if (entity == null) {
            log.warn("Attempted to map a null RwfisEntity to RwfisDTO. Returning null DTO.");
            return null; // Or throw an IllegalArgumentException if null input is not allowed
        }

        log.info("Starting mapping of RwfisEntity with ID {} to RwfisDTO.", entity.getId());

        RwfisDTO dto = new RwfisDTO();
        dto.setId(entity.getId());
        dto.setStartChainageKm(entity.getStartChainageKm());
        dto.setEndChainageKm(entity.getEndChainageKm());
        dto.setCrossSectionLocation(entity.getCrossSectionLocation());
        dto.setOffsetM(entity.getOffsetM());
        dto.setFeature(entity.getFeature());
        dto.setMaterialType(entity.getMaterialType());
        dto.setFeatureCondition(entity.getFeatureCondition());
        dto.setSafetyHazard(entity.isSafetyHazard());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());
        dto.setAltitude(entity.getAltitude());
        dto.setSurveyDate(entity.getSurveyDate());
        dto.setRemarks(entity.getRemarks());

        log.info("Successfully mapped RwfisEntity with ID {} to RwfisDTO.", entity.getId());
        return dto;
    }
    public RsiDTO mapToDto(RwfisEntity entity){
        // Adding a null check for robustness.
        if (entity == null) {
            log.warn("Attempted to map a null RwfisEntity to RsiDTO. Returning null DTO.");
            return null; // Or throw an IllegalArgumentException if null input is not allowed
        }

        // Log the start of the mapping process.
        // Assuming entity has an ID or some unique identifier for logging context.
        log.info("Starting mapping of RwfisEntity (for RSI) with ID {} to RsiDTO.", entity.getId());

        RsiDTO dto = new RsiDTO();
        dto.setStartChainageKm(entity.getStartChainageKm());
        dto.setEndChainageKm(entity.getEndChainageKm());
        dto.setCrossSectionLocation(entity.getCrossSectionLocation());
        dto.setFeature(entity.getFeature());
        dto.setSafetyHazard(entity.isSafetyHazard());
        dto.setFeatureCondition(entity.getFeatureCondition());
        dto.setInsertedBy(entity.getCreatedBy()); // Assuming 'InsertedBy' in RsiDTO maps to 'CreatedBy' in RwfisEntity
        dto.setLastUpdatedDate(entity.getLastupdatedDate());

        // Log the successful completion of the mapping.
        log.info("Successfully mapped RwfisEntity with ID {} to RsiDTO.", entity.getId());
        return dto;
    }
    @Transactional
    public RwfisDTO update(Long id, RwfisEntity newData, Long nid) {
        log.info("Attempting to update Rwfis entity with ID: {} by NID: {}.", id, nid);
        try {
            // Retrieve the existing entity using the service's getById method.
            // This method is designed to return null if the entity is not found.
            RwfisEntity existing = getById(id);

            // If the entity to be updated is not found, throw an exception.
            // This RuntimeException will be caught by the controller's generic Exception handler,
            // resulting in a 500 Internal Server Error response, consistent with the current pattern for non-CustomExceptions.
            if (existing == null) {
                log.warn("Rwfis entity not found for update with ID: {}. Cannot proceed with update.", id);
                throw new RuntimeException("Rwfis entity not found with ID: " + id + " for update operation.");
            }

            // --- Data Copying and Update ---
            // Update fields from newData to existing entity
            existing.setStartChainageKm(newData.getStartChainageKm());
            existing.setEndChainageKm(newData.getEndChainageKm());
            existing.setCrossSectionLocation(newData.getCrossSectionLocation());
            existing.setOffsetM(newData.getOffsetM());
            existing.setFeature(newData.getFeature());
            existing.setMaterialType(newData.getMaterialType());
            existing.setFeatureCondition(newData.getFeatureCondition());
            existing.setSafetyHazard(newData.isSafetyHazard());
            existing.setLatitude(newData.getLatitude());
            existing.setLongitude(newData.getLongitude());
            existing.setAltitude(newData.getAltitude());
            existing.setSurveyDate(newData.getSurveyDate());
            existing.setRemarks(newData.getRemarks());
            existing.setDeleted(newData.isDeleted()); // Assuming 'deleted' status can also be updated here

            // --- Audit Fields Update ---
            existing.setLastupdatedBy(nid);
            existing.setLastupdatedDate(LocalDate.now()); // Set current date as last updated date
            log.debug("Rwfis entity with ID {} details populated for update. Last updated by: {}.", id, nid);

            // Save the updated entity
            RwfisEntity curr = repository.save(existing);
            log.info("Rwfis entity with ID {} successfully saved after update.", curr.getId());

            // Map the updated entity to DTO and return
            RwfisDTO updatedDto = mapToDTO(curr);
            log.info("Rwfis entity with ID {} successfully updated and mapped to DTO.", updatedDto.getId());
            return updatedDto;

        } catch (Exception e) {
            // Log any unexpected errors that occur during the update process
            log.error("Error occurred while updating Rwfis entity with ID {}: {}", id, e.getMessage(), e);
            // Re-throw a generic RuntimeException. This will be caught by the controller's
            // generic 'catch (Exception e)' block, which will return a 500 INTERNAL_SERVER_ERROR.
            throw new RuntimeException("Failed to update Rwfis entity with ID " + id + ": " + e.getMessage(), e);
        }
    }

    @Transactional // Deletion operations should typically be transactional
    public void delete(Long id) {
        log.info("Attempting to hard delete Rwfis entity with ID: {}.", id);
        try {
            // Check if the entity exists before attempting to delete, to provide a more specific message
            // or let EmptyResultDataAccessException handle it.
            // Using findById to check existence allows for more controlled error messages.
            if (!repository.existsById(id)) {
                log.warn("Rwfis entity not found for hard delete with ID: {}.", id);
                throw new RuntimeException("Rwfis entity not found with ID: " + id + " for deletion.");
            }

            repository.deleteById(id);
            log.info("Successfully hard deleted Rwfis entity with ID: {}.", id);
        } catch (EmptyResultDataAccessException e) {
            // This specific exception occurs if deleteById is called on a non-existent ID
            log.error("Rwfis entity with ID {} not found for deletion: {}", id, e.getMessage());
            throw new RuntimeException("Rwfis entity not found with ID: " + id + " for deletion.", e);
        } catch (Exception e) {
            // Catch any other unexpected errors during the deletion process
            log.error("Error occurred while hard deleting Rwfis entity with ID {}: {}", id, e.getMessage(), e);
            // Re-throw a generic RuntimeException, which the controller's catch(Exception e) will handle.
            throw new RuntimeException("Failed to hard delete Rwfis entity with ID " + id + ": " + e.getMessage(), e);
        }
    }
}
