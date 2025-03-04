package com.sadeem.smap.service;

import com.sadeem.smap.dto.NfcDto;
import com.sadeem.smap.model.NfcDevice;
import com.sadeem.smap.repository.NfcRepository;
import com.sadeem.smap.util.EventResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NfcService {

    @Autowired
    private NfcRepository nfcRepository;

    public Iterable<NfcDto> getAllNfcs() {
        return nfcRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<NfcDto> getNfcsPaginated(Pageable pageable) {
        return nfcRepository.findAll(pageable).map(this::convertToDto);
    }

    public Optional<NfcDto> getNfcById(Long id) {
        return nfcRepository.findById(id).map(this::convertToDto);
    }

    public EventResult createNfc(NfcDto nfcDto) {
        NfcDevice nfcDevice = convertToEntity(nfcDto);
        nfcRepository.save(nfcDevice);
        return new EventResult("success", "NFC created successfully");
    }

    public EventResult updateNfc(NfcDto nfcDto) {
        Optional<NfcDevice> optionalNfc = nfcRepository.findById(nfcDto.getId());
        if (optionalNfc.isPresent()) {
            NfcDevice nfcDevice = optionalNfc.get();
            nfcDevice.setName(nfcDto.getName());
            nfcDevice.setMacAddress(nfcDto.getMacAddress());
            nfcDevice.setDescription(nfcDto.getDescription());
            nfcDevice.setFloorNumber(nfcDto.getFloorNumber());
            nfcRepository.save(nfcDevice);
            return new EventResult("success", "NFC updated successfully");
        }
        return new EventResult("error", "NFC not found");
    }

    public EventResult deleteNfc(Long id) {
        nfcRepository.deleteById(id);
        return new EventResult("success", "NFC deleted successfully");
    }

    public void deleteAllNfcs() {
        nfcRepository.deleteAll();
    }

    private NfcDto convertToDto(NfcDevice nfcDevice) {
        NfcDto dto = new NfcDto();
        dto.setId(nfcDevice.getId());
        dto.setName(nfcDevice.getName());
        dto.setMacAddress(nfcDevice.getMacAddress());
        dto.setDescription(nfcDevice.getDescription());
        dto.setFloorNumber(nfcDevice.getFloorNumber());
        return dto;
    }

    private NfcDevice convertToEntity(NfcDto nfcDto) {
        NfcDevice nfcDevice = new NfcDevice();
        nfcDevice.setId(nfcDto.getId());
        nfcDevice.setName(nfcDto.getName());
        nfcDevice.setMacAddress(nfcDto.getMacAddress());
        nfcDevice.setDescription(nfcDto.getDescription());
        nfcDevice.setFloorNumber(nfcDto.getFloorNumber());
        return nfcDevice;
    }
}