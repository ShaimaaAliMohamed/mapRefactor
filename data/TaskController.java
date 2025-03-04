package com.sadeem.smap.controller;

import com.sadeem.smap.dto.TaskDto;
import com.sadeem.smap.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * Get all tasks.
     */
    @GetMapping
    public ResponseEntity<Iterable<TaskDto>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    /**
     * Get paginated list of tasks.
     */
    @GetMapping("/list")
    public ResponseEntity<Page<TaskDto>> getTasksPaginated(Pageable pageable) {
        return ResponseEntity.ok(taskService.getTasksPaginated(pageable));
    }

    /**
     * Get task by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id, Locale locale) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new task.
     */
    @PostMapping("/add")
    public ResponseEntity<Void> createTask(@RequestBody TaskDto taskDto, Locale locale) {
        taskService.createTask(taskDto);
        return ResponseEntity.status(201).build();
    }

    /**
     * Update an existing task.
     */
    @PutMapping("/edit")
    public ResponseEntity<Void> updateTask(@RequestBody TaskDto taskDto, Locale locale) {
        taskService.updateTask(taskDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a task by ID.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, Locale locale) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}