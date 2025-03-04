package com.sadeem.smap.service;

import com.sadeem.smap.dto.RawMaterialDto;
import com.sadeem.smap.model.RawMaterial;
import com.sadeem.smap.repository.RawMaterialRepository;
import com.sadeem.smap.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RawMaterialService {

    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    @Autowired
    private UnitRepository unitRepository;

    public Iterable<RawMaterialDto> getAllRawMaterials() {
        return rawMaterialRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<RawMaterialDto> getRawMaterialsPaginated(Pageable pageable) {
        return rawMaterialRepository.findAll(pageable).map(this::convertToDto);
    }

    public Optional<RawMaterialDto> getRawMaterialById(Long id) {
        return rawMaterialRepository.findById(id).map(this::convertToDto);
    }

    public void createRawMaterial(RawMaterialDto rawMaterialDto) {
        RawMaterial rawMaterial = convertToEntity(rawMaterialDto);
        rawMaterialRepository.save(rawMaterial);
    }

    public void updateRawMaterial(RawMaterialDto rawMaterialDto) {
        Optional<RawMaterial> optionalRawMaterial = rawMaterialRepository.findById(rawMaterialDto.getId());
        if (optionalRawMaterial.isPresent()) {
            RawMaterial rawMaterial = optionalRawMaterial.get();
            rawMaterial.setName(rawMaterialDto.getName());
            rawMaterial.setUnit(unitRepository.findById(rawMaterialDto.getUnitId()).orElse(null));
            rawMaterialRepository.save(rawMaterial);
        }
    }

    public void deleteRawMaterial(Long id) {
        rawMaterialRepository.deleteById(id);
    }

    public void deleteAllRawMaterials() {
        rawMaterialRepository.deleteAll();
    }

    private RawMaterialDto convertToDto(RawMaterial rawMaterial) {
        RawMaterialDto dto = new RawMaterialDto();
        dto.setId(rawMaterial.getId());
        dto.setName(rawMaterial.getName());
        dto.setUnitId(rawMaterial.getUnit().getId());
        return dto;
    }

    private RawMaterial convertToEntity(RawMaterialDto rawMaterialDto) {
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setId(rawMaterialDto.getId());
        rawMaterial.setName(rawMaterialDto.getName());
        rawMaterial.setUnit(unitRepository.findById(rawMaterialDto.getUnitId()).orElse(null));
        return rawMaterial;
    }
}