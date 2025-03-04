package com.sadeem.smap.controller;

import com.sadeem.smap.dto.GroupDto;
import com.sadeem.smap.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    /**
     * Get all groups.
     */
    @GetMapping
    public ResponseEntity<Iterable<GroupDto>> getAllGroups() {
        return ResponseEntity.ok(groupService.getAllGroups());
    }

    /**
     * Get paginated list of groups.
     */
    @GetMapping("/list")
    public ResponseEntity<Page<GroupDto>> getGroupsPaginated(Pageable pageable) {
        return ResponseEntity.ok(groupService.getGroupsPaginated(pageable));
    }

    /**
     * Get group by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> getGroupById(@PathVariable Long id, Locale locale) {
        return groupService.getGroupById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new group.
     */
    @PostMapping("/add")
    public ResponseEntity<Void> createGroup(@RequestBody GroupDto groupDto, Locale locale) {
        groupService.createGroup(groupDto);
        return ResponseEntity.status(201).build();
    }

    /**
     * Update an existing group.
     */
    @PutMapping("/edit")
    public ResponseEntity<Void> updateGroup(@RequestBody GroupDto groupDto, Locale locale) {
        groupService.updateGroup(groupDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a group by ID.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id, Locale locale) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }
}