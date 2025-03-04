package com.sadeem.smap.controller;

import com.sadeem.smap.dto.SkillDto;
import com.sadeem.smap.service.SkillService;
import com.sadeem.smap.util.EventResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;

@RestController
@RequestMapping("/api/skills")
public class SkillEventController {

    @Autowired
    private SkillService skillService;

    @Autowired
    private MessageSource messageSource;

    /**
     * Create a new skill.
     */
    @PostMapping("/add")
    public ResponseEntity<EventResult> addSkill(
            @Valid @RequestBody SkillDto skillDto,
            BindingResult bindingResult,
            Locale locale) {
        if (bindingResult.hasErrors()) {
            String errorMessage = messageSource.getMessage("invalid_input", null, locale);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new EventResult("error", errorMessage));
        }
        try {
            EventResult result = skillService.createSkill(skillDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("critical_error", null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResult("error", errorMessage + ": " + e.getMessage()));
        }
    }

    /**
     * Update an existing skill.
     */
    @PutMapping("/edit")
    public ResponseEntity<EventResult> editSkill(
            @Valid @RequestBody SkillDto skillDto,
            BindingResult bindingResult,
            Locale locale) {
        if (bindingResult.hasErrors()) {
            String errorMessage = messageSource.getMessage("invalid_input", null, locale);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new EventResult("error", errorMessage));
        }
        try {
            EventResult result = skillService.updateSkill(skillDto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("critical_error", null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResult("error", errorMessage + ": " + e.getMessage()));
        }
    }

    /**
     * Delete a skill by ID.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<EventResult> deleteSkill(@PathVariable Long id, Locale locale) {
        try {
            EventResult result = skillService.deleteSkill(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("critical_error", null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResult("error", errorMessage + ": " + e.getMessage()));
        }
    }

    /**
     * Delete all skills.
     */
    @DeleteMapping("/delete-all")
    public ResponseEntity<Void> deleteAllSkills(Locale locale) {
        try {
            skillService.deleteAllSkills();
            String successMessage = messageSource.getMessage("skills.deleted.successfully", null, locale);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("critical_error", null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResult("error", errorMessage + ": " + e.getMessage()));
        }
    }
}