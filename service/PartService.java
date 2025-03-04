package com.sadeem.smap.service;

import com.sadeem.smap.dto.PartDto;
import com.sadeem.smap.model.Dimension;
import com.sadeem.smap.model.Part;
import com.sadeem.smap.repository.PartRepository;
import com.sadeem.smap.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PartService {

    @Autowired
    private PartRepository partRepository;

    public Iterable<PartDto> getAllParts() {
        return partRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<PartDto> getPartById(Long id) {
        return partRepository.findById(id).map(this::convertToDto);
    }

    public void createPart(PartDto partDto, MultipartFile file) throws IOException {
        Part part = convertToEntity(partDto);
        partRepository.save(part);
        FileUtil.writeToFile(file, "/opt/images/parts/" + part.getPartId() + ".jpg");
    }

    public void updatePart(PartDto partDto, MultipartFile file) throws IOException {
        Optional<Part> optionalPart = partRepository.findById(partDto.getId());
        if (optionalPart.isPresent()) {
            Part part = optionalPart.get();
            Dimension dimension = part.getDimensionByDimensionBeforeId();
            dimension.setDepth(partDto.getLength());
            dimension.setWidth(partDto.getWidth());
            dimension.setHeight(partDto.getHeight());

            part.setPartName(partDto.getName());
            part.setDimensionByDimensionBeforeId(dimension);

            if (file != null && !file.isEmpty()) {
                FileUtil.writeToFile(file, "/opt/images/parts/" + part.getPartId() + ".jpg");
            }

            partRepository.save(part);
        }
    }

    public void deletePart(Long id) {
        partRepository.deleteById(id);
    }

    public void deleteAllParts() {
        partRepository.deleteAll();
    }

    private PartDto convertToDto(Part part) {
        PartDto dto = new PartDto();
        dto.setId(part.getPartId());
        dto.setName(part.getPartName());
        dto.setLength(part.getDimensionByDimensionBeforeId().getDepth());
        dto.setWidth(part.getDimensionByDimensionBeforeId().getWidth());
        dto.setHeight(part.getDimensionByDimensionBeforeId().getHeight());
        return dto;
    }

    private Part convertToEntity(PartDto partDto) {
        Part part = new Part();
        part.setPartId(partDto.getId());
        part.setPartName(partDto.getName());
        Dimension dimension = new Dimension();
        dimension.setDepth(partDto.getLength());
        dimension.setWidth(partDto.getWidth());
        dimension.setHeight(partDto.getHeight());
        part.setDimensionByDimensionBeforeId(dimension);
        return part;
    }
}