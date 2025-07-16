package com.RAMS.RWFIS.Service;

import com.RAMS.RWFIS.DTO.RsiDTO;
import com.RAMS.RWFIS.DTO.RwfisDTO;
import com.RAMS.RWFIS.Entity.RwfisEntity;
import com.RAMS.RWFIS.Repository.RwfisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RwfisService {

    @Autowired
    private RwfisRepository repository;

    public List<RwfisEntity> getAll() {
        return repository.findAll();
    }
    public RwfisEntity getByRsiId(Long id) {
        return repository.findByRsiId(id)
                .orElseThrow(() -> new RuntimeException("Rwfis not found with secondary ID " + id));
    }

    public RwfisEntity getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rwfis not found with id " + id));
    }
    @Transactional
    public RwfisDTO create(RwfisEntity rwfis) {

        RwfisEntity curr = repository.save(rwfis);
        if (curr.getCreatedBy() == null) {
            curr.setCreatedBy(curr.getId());
            curr = repository.save(curr);
        }
        return mapToDTO(curr);
    }
    public RwfisDTO mapToDTO(RwfisEntity entity) {
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
        return dto;
    }
    public RsiDTO mapToDto(RwfisEntity entity){
        RsiDTO dto = new RsiDTO();
        dto.setStartChainageKm(entity.getStartChainageKm());
        dto.setEndChainageKm(entity.getEndChainageKm());
        dto.setCrossSectionLocation(entity.getCrossSectionLocation());
        dto.setFeature(entity.getFeature());
        dto.setSafetyHazard(entity.isSafetyHazard());
        dto.setFeatureCondition(entity.getFeatureCondition());
        dto.setInsertedBy(entity.getCreatedBy());
        dto.setLastUpdatedDate(entity.getLastupdatedDate());
        return dto;
    }
    public RwfisDTO update(Long id, RwfisEntity newData,Long nid) {
        RwfisEntity existing = getById(id);
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
        existing.setDeleted(newData.isDeleted());
        existing.setLastupdatedBy(nid);
        existing.setLastupdatedDate(LocalDate.now());
        RwfisEntity curr = repository.save(existing);
        return mapToDTO(curr);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
