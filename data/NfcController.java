package com.sadeem.smap.controller;

import com.sadeem.smap.service.NfcService;
import com.sadeem.smap.dto.NfcDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/nfcs")
public class NfcController {

    @Autowired
    private NfcService nfcService;

    /**
     * Get all NFC devices (general route).
     */
    @GetMapping
    public ResponseEntity<Iterable<NfcDto>> getAllNfcs() {
        return ResponseEntity.ok(nfcService.getAllNfcs());
    }

    /**
     * Get a paginated list of NFC devices.
     */
    @GetMapping("/list")
    public ResponseEntity<Page<NfcDto>> getNfcsPaginated(Pageable pageable) {
        return ResponseEntity.ok(nfcService.getNfcsPaginated(pageable));
    }

    /**
     * Get a specific NFC device by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NfcDto> getNfcById(@PathVariable Long id, Locale locale) {
        return nfcService.getNfcById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}