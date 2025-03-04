package com.sadeem.smap.service;

import com.sadeem.smap.dto.AccessoriesDto;
import com.sadeem.smap.model.Accessories;
import com.sadeem.smap.repository.AccessoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccessoriesService {

    @Autowired
    private AccessoriesRepository accessoriesRepository;

    public Iterable<AccessoriesDto> getAllAccessories() {
        return accessoriesRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<AccessoriesDto> getAccessoriesPaginated(Pageable pageable) {
        return accessoriesRepository.findAll(pageable).map(this::convertToDto);
    }

    public Optional<AccessoriesDto> getAccessoryById(Long id) {
        return accessoriesRepository.findById(id).map(this::convertToDto);
    }

    public void createAccessory(AccessoriesDto accessoryDto) {
        Accessories accessory = convertToEntity(accessoryDto);
        accessoriesRepository.save(accessory);
    }

    public void updateAccessory(AccessoriesDto accessoryDto) {
        Accessories accessory = convertToEntity(accessoryDto);
        accessoriesRepository.save(accessory);
    }

    public void deleteAccessory(Long id) {
        accessoriesRepository.deleteById(id);
    }

    private AccessoriesDto convertToDto(Accessories accessory) {
        AccessoriesDto dto = new AccessoriesDto();
        dto.setId(accessory.getId());
        dto.setAccessoriesCode(accessory.getAccessoriesCode());
        dto.setAccessoriesName(accessory.getAccessoriesName());
        dto.setAccessoriesDescription(accessory.getAccessoriesDescription());
        return dto;
    }

    private Accessories convertToEntity(AccessoriesDto accessoryDto) {
        Accessories accessory = new Accessories();
        accessory.setId(accessoryDto.getId());
        accessory.setAccessoriesCode(accessoryDto.getAccessoriesCode());
        accessory.setAccessoriesName(accessoryDto.getAccessoriesName());
        accessory.setAccessoriesDescription(accessoryDto.getAccessoriesDescription());
        return accessory;
    }
}