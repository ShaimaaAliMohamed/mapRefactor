package com.sadeem.smap.service;

import com.sadeem.smap.dto.ReasonDetailsDto;
import com.sadeem.smap.model.Reason;
import com.sadeem.smap.model.ReasonDetails;
import com.sadeem.smap.repository.ReasonDetailsRepository;
import com.sadeem.smap.repository.ReasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReasonDetailsService {

    @Autowired
    private ReasonDetailsRepository reasonDetailsRepository;

    @Autowired
    private ReasonRepository reasonRepository;

    public Iterable<ReasonDetailsDto> getAllReasonDetails() {
        return reasonDetailsRepository.findAllByIsDeletedFalse().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<ReasonDetailsDto> getReasonDetailsPaginated(Pageable pageable) {
        return reasonDetailsRepository.findAllByIsDeletedFalse(pageable).map(this::convertToDto);
    }

    public Optional<ReasonDetailsDto> getReasonDetailById(Long id) {
        return reasonDetailsRepository.findById(id).map(this::convertToDto);
    }

    public void createReasonDetail(ReasonDetailsDto reasonDetailsDto) {
        ReasonDetails reasonDetails = convertToEntity(reasonDetailsDto);
        reasonDetails.setIsDeleted(false);
        reasonDetailsRepository.save(reasonDetails);
    }

    public void updateReasonDetail(ReasonDetailsDto reasonDetailsDto) {
        Optional<ReasonDetails> optionalReasonDetails = reasonDetailsRepository.findById(reasonDetailsDto.getId());
        if (optionalReasonDetails.isPresent()) {
            ReasonDetails reasonDetails = optionalReasonDetails.get();
            reasonDetails.setName(reasonDetailsDto.getName());
            reasonDetails.setReason(reasonRepository.findById(reasonDetailsDto.getReasonId()).orElseThrow());
            reasonDetails.setIsDeleted(false);
            reasonDetailsRepository.save(reasonDetails);
        }
    }

    public void deleteReasonDetail(Long id) {
        Optional<ReasonDetails> optionalReasonDetails = reasonDetailsRepository.findById(id);
        if (optionalReasonDetails.isPresent()) {
            ReasonDetails reasonDetails = optionalReasonDetails.get();
            reasonDetails.setIsDeleted(true);
            reasonDetailsRepository.save(reasonDetails);
        }
    }

    private ReasonDetailsDto convertToDto(ReasonDetails reasonDetails) {
        ReasonDetailsDto dto = new ReasonDetailsDto();
        dto.setId(reasonDetails.getId());
        dto.setReasonId(reasonDetails.getReason().getId());
        dto.setName(reasonDetails.getName());
        dto.setReasonName(reasonDetails.getReason().getReasonName());
        return dto;
    }

    private ReasonDetails convertToEntity(ReasonDetailsDto reasonDetailsDto) {
        ReasonDetails reasonDetails = new ReasonDetails();
        reasonDetails.setId(reasonDetailsDto.getId());
        reasonDetails.setName(reasonDetailsDto.getName());
        reasonDetails.setReason(reasonRepository.findById(reasonDetailsDto.getReasonId()).orElseThrow());
        return reasonDetails;
    }
}