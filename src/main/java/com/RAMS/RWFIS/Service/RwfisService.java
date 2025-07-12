package com.RAMS.RWFIS.Service;

import com.RAMS.RWFIS.Entity.RwfisEntity;
import com.RAMS.RWFIS.Repository.RwfisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RwfisService {

    @Autowired
    private RwfisRepository repository;

    public List<RwfisEntity> getAll() {
        return repository.findAll();
    }

    public RwfisEntity getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rwfis not found with id " + id));
    }

    public RwfisEntity create(RwfisEntity rwfis) {
        return repository.save(rwfis);
    }

    public RwfisEntity update(Long id, RwfisEntity newData) {
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

        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
