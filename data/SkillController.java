package com.sadeem.smap.controller;

import com.sadeem.smap.service.SkillService;
import com.sadeem.smap.dto.SkillDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    /**
     * Get all skills (general route).
     */
    @GetMapping
    public ResponseEntity<Iterable<SkillDto>> getAllSkills() {
        return ResponseEntity.ok(skillService.getAllSkills());
    }

    /**
     * Get a paginated list of skills.
     */
    @GetMapping("/list")
    public ResponseEntity<Page<SkillDto>> getSkillsPaginated(Pageable pageable) {
        return ResponseEntity.ok(skillService.getSkillsPaginated(pageable));
    }

    /**
     * Get a specific skill by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SkillDto> getSkillById(@PathVariable Long id, Locale locale) {
        return skillService.getSkillById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}