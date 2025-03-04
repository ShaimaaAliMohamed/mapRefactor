package com.sadeem.smap.service;

import com.sadeem.smap.dto.TaskDto;
import com.sadeem.smap.model.Department;
import com.sadeem.smap.model.Task;
import com.sadeem.smap.repository.DepartmentRepository;
import com.sadeem.smap.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public Iterable<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<TaskDto> getTasksPaginated(Pageable pageable) {
        return taskRepository.findAll(pageable).map(this::convertToDto);
    }

    public Optional<TaskDto> getTaskById(Long id) {
        return taskRepository.findById(id).map(this::convertToDto);
    }

    public void createTask(TaskDto taskDto) {
        Task task = convertToEntity(taskDto);
        taskRepository.save(task);
    }

    public void updateTask(TaskDto taskDto) {
        Optional<Task> optionalTask = taskRepository.findById(taskDto.getId());
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setDepartment(departmentRepository.findById(taskDto.getDepartmentId()).orElseThrow());
            task.setTaskName(taskDto.getName());
            task.setTaskDescription(taskDto.getDescription());
            task.setTaskCode(taskDto.getCode());
            taskRepository.save(task);
        }
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    private TaskDto convertToDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setName(task.getTaskName());
        dto.setDescription(task.getTaskDescription());
        dto.setCode(task.getTaskCode());
        dto.setDepartmentId(task.getDepartment().getId());
        return dto;
    }

    private Task convertToEntity(TaskDto taskDto) {
        Task task = new Task();
        task.setId(taskDto.getId());
        task.setDepartment(departmentRepository.findById(taskDto.getDepartmentId()).orElseThrow());
        task.setTaskName(taskDto.getName());
        task.setTaskDescription(taskDto.getDescription());
        task.setTaskCode(taskDto.getCode());
        return task;
    }
}