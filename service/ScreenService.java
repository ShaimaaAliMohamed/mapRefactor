package com.sadeem.smap.service;

import com.sadeem.smap.dto.ScreenDto;
import com.sadeem.smap.model.Department;
import com.sadeem.smap.model.Screen;
import com.sadeem.smap.repository.DepartmentRepository;
import com.sadeem.smap.repository.ScreenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScreenService {

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public Iterable<ScreenDto> getAllScreens() {
        return screenRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<ScreenDto> getScreensPaginated(Pageable pageable) {
        return screenRepository.findAll(pageable).map(this::convertToDto);
    }

    public Optional<ScreenDto> getScreenById(Long id) {
        return screenRepository.findById(id).map(this::convertToDto);
    }

    public void createScreen(ScreenDto screenDto) {
        Screen screen = convertToEntity(screenDto);
        screenRepository.save(screen);
    }

    public void updateScreen(ScreenDto screenDto) {
        Optional<Screen> optionalScreen = screenRepository.findById(screenDto.getId());
        if (optionalScreen.isPresent()) {
            Screen screen = optionalScreen.get();
            screen.setDepartment(departmentRepository.findById(screenDto.getDepartmentId()).orElseThrow());
            screen.setScreenName(screenDto.getName());
            screen.setScreenIp(screenDto.getIp());
            screenRepository.save(screen);
        }
    }

    public void deleteScreen(Long id) {
        screenRepository.deleteById(id);
    }

    private ScreenDto convertToDto(Screen screen) {
        ScreenDto dto = new ScreenDto();
        dto.setId(screen.getId());
        dto.setName(screen.getScreenName());
        dto.setIp(screen.getScreenIp());
        dto.setDepartmentId(screen.getDepartment().getId());
        return dto;
    }

    private Screen convertToEntity(ScreenDto screenDto) {
        Screen screen = new Screen();
        screen.setId(screenDto.getId());
        screen.setDepartment(departmentRepository.findById(screenDto.getDepartmentId()).orElseThrow());
        screen.setScreenName(screenDto.getName());
        screen.setScreenIp(screenDto.getIp());
        return screen;
    }
}