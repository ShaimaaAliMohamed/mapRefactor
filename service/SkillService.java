package com.sadeem.smap.service;

import com.sadeem.smap.dto.SkillDto;
import com.sadeem.smap.model.Skill;
import com.sadeem.smap.repository.SkillRepository;
import com.sadeem.smap.util.EventResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    public Iterable<SkillDto> getAllSkills() {
        return skillRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<SkillDto> getSkillsPaginated(Pageable pageable) {
        return skillRepository.findAll(pageable).map(this::convertToDto);
    }

    public Optional<SkillDto> getSkillById(Long id) {
        return skillRepository.findById(id).map(this::convertToDto);
    }

    public EventResult createSkill(SkillDto skillDto) {
        Skill skill = convertToEntity(skillDto);
        skillRepository.save(skill);
        return new EventResult("success", "Skill created successfully");
    }

    public EventResult updateSkill(SkillDto skillDto) {
        Optional<Skill> optionalSkill = skillRepository.findById(skillDto.getId());
        if (optionalSkill.isPresent()) {
            Skill skill = optionalSkill.get();
            skill.setSkillName(skillDto.getName());
            skillRepository.save(skill);
            return new EventResult("success", "Skill updated successfully");
        }
        return new EventResult("error", "Skill not found");
    }

    public EventResult deleteSkill(Long id) {
        skillRepository.deleteById(id);
        return new EventResult("success", "Skill deleted successfully");
    }

    public void deleteAllSkills() {
        skillRepository.deleteAll();
    }

    private SkillDto convertToDto(Skill skill) {
        SkillDto dto = new SkillDto();
        dto.setId(skill.getId());
        dto.setName(skill.getSkillName());
        return dto;
    }

    private Skill convertToEntity(SkillDto skillDto) {
        Skill skill = new Skill();
        skill.setId(skillDto.getId());
        skill.setSkillName(skillDto.getName());
        return skill;
    }
}