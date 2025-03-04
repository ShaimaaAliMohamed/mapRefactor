package com.sadeem.smap.service;

import com.sadeem.smap.dto.UnitDto;
import com.sadeem.smap.model.Unit;
import com.sadeem.smap.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeasurementService {

    @Autowired
    private UnitRepository unitRepository;

    public Iterable<UnitDto> getAllUnits() {
        return unitRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<UnitDto> getUnitsPaginated(Pageable pageable) {
        return unitRepository.findAll(pageable).map(this::convertToDto);
    }

    public Optional<UnitDto> getUnitById(Long id) {
        return unitRepository.findById(id).map(this::convertToDto);
    }

    public void createUnit(UnitDto unitDto) {
        Unit unit = convertToEntity(unitDto);
        unitRepository.save(unit);
    }

    public void updateUnit(UnitDto unitDto) {
        Optional<Unit> optionalUnit = unitRepository.findById(unitDto.getId());
        if (optionalUnit.isPresent()) {
            Unit unit = optionalUnit.get();
            unit.setUnitName(unitDto.getName());
            unitRepository.save(unit);
        }
    }

    public void deleteUnit(Long id) {
        unitRepository.deleteById(id);
    }

    public void deleteAllUnits() {
        unitRepository.deleteAll();
    }

    private UnitDto convertToDto(Unit unit) {
        UnitDto dto = new UnitDto();
        dto.setId(unit.getId());
        dto.setName(unit.getUnitName());
        return dto;
    }

    private Unit convertToEntity(UnitDto unitDto) {
        Unit unit = new Unit();
        unit.setId(unitDto.getId());
        unit.setUnitName(unitDto.getName());
        return unit;
    }
}