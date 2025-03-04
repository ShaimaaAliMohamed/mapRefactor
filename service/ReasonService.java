package com.sadeem.smap.service;

import com.sadeem.smap.dto.ReasonDto;
import com.sadeem.smap.model.Reason;
import com.sadeem.smap.repository.ReasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReasonService {

    @Autowired
    private ReasonRepository reasonRepository;

    public Iterable<ReasonDto> getAllReasons() {
        return reasonRepository.findAllByIsDeletedFalse().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<ReasonDto> getReasonsPaginated(Pageable pageable) {
        return reasonRepository.findAllByIsDeletedFalse(pageable).map(this::convertToDto);
    }

    public Optional<ReasonDto> getReasonById(Long id) {
        return reasonRepository.findById(id).map(this::convertToDto);
    }

    public void createReason(ReasonDto reasonDto) {
        Reason reason = convertToEntity(reasonDto);
        reason.setIsDeleted(false);
        reasonRepository.save(reason);
    }

    public void updateReason(ReasonDto reasonDto) {
        Optional<Reason> optionalReason = reasonRepository.findById(reasonDto.getId());
        if (optionalReason.isPresent()) {
            Reason reason = optionalReason.get();
            reason.setReasonName(reasonDto.getName());
            reason.setIsDeleted(false);
            reasonRepository.save(reason);
        }
    }

    public void deleteReason(Long id) {
        Optional<Reason> optionalReason = reasonRepository.findById(id);
        if (optionalReason.isPresent()) {
            Reason reason = optionalReason.get();
            reason.setIsDeleted(true);
            reasonRepository.save(reason);
        }
    }

    private ReasonDto convertToDto(Reason reason) {
        ReasonDto dto = new ReasonDto();
        dto.setId(reason.getId());
        dto.setName(reason.getReasonName());
        return dto;
    }

    private Reason convertToEntity(ReasonDto reasonDto) {
        Reason reason = new Reason();
        reason.setId(reasonDto.getId());
        reason.setReasonName(reasonDto.getName());
        return reason;
    }
}