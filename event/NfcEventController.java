package com.sadeem.smap.controller;

import com.sadeem.smap.dto.NfcDto;
import com.sadeem.smap.service.NfcService;
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
@RequestMapping("/api/nfcs")
public class NfcEventController {

    @Autowired
    private NfcService nfcService;

    @Autowired
    private MessageSource messageSource;

    /**
     * Create a new NFC device.
     */
    @PostMapping("/add")
    public ResponseEntity<EventResult> addNfc(
            @Valid @RequestBody NfcDto nfcDto,
            BindingResult bindingResult,
            Locale locale) {
        if (bindingResult.hasErrors()) {
            String errorMessage = messageSource.getMessage("invalid_input", null, locale);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new EventResult("error", errorMessage));
        }
        try {
            EventResult result = nfcService.createNfc(nfcDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("critical_error", null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResult("error", errorMessage + ": " + e.getMessage()));
        }
    }

    /**
     * Update an existing NFC device.
     */
    @PutMapping("/edit")
    public ResponseEntity<EventResult> editNfc(
            @Valid @RequestBody NfcDto nfcDto,
            BindingResult bindingResult,
            Locale locale) {
        if (bindingResult.hasErrors()) {
            String errorMessage = messageSource.getMessage("invalid_input", null, locale);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new EventResult("error", errorMessage));
        }
        try {
            EventResult result = nfcService.updateNfc(nfcDto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("critical_error", null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResult("error", errorMessage + ": " + e.getMessage()));
        }
    }

    /**
     * Delete an NFC device by ID.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<EventResult> deleteNfc(@PathVariable Long id, Locale locale) {
        try {
            EventResult result = nfcService.deleteNfc(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("critical_error", null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResult("error", errorMessage + ": " + e.getMessage()));
        }
    }

    /**
     * Delete all NFC devices.
     */
    @DeleteMapping("/delete-all")
    public ResponseEntity<Void> deleteAllNfcs(Locale locale) {
        try {
            nfcService.deleteAllNfcs();
            String successMessage = messageSource.getMessage("nfcs.deleted.successfully", null, locale);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("critical_error", null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResult("error", errorMessage + ": " + e.getMessage()));
        }
    }
}