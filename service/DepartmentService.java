package com.sadeem.smap.service;

import com.sadeem.smap.dto.DepartmentDto;
import com.sadeem.smap.model.Department;
import com.sadeem.smap.repository.DepartmentRepository;
import com.sadeem.smap.util.EventResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public EventResult createDepartment(DepartmentDto departmentDto) {
        Department department = convertToEntity(departmentDto);
        departmentRepository.save(department);
        return new EventResult("success", "Department created successfully");
    }

    public EventResult updateDepartment(DepartmentDto departmentDto) {
        Optional<Department> optionalDepartment = departmentRepository.findById(departmentDto.getId());
        if (optionalDepartment.isPresent()) {
            Department department = optionalDepartment.get();
            department.setDepartmentName(departmentDto.getName());
            department.setFloorNumber(departmentDto.getFloorNumber());
            departmentRepository.save(department);
            return new EventResult("success", "Department updated successfully");
        }
        return new EventResult("error", "Department not found");
    }

    public EventResult deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
        return new EventResult("success", "Department deleted successfully");
    }

    public void deleteAllDepartments() {
        departmentRepository.deleteAll();
    }

    private Department convertToEntity(DepartmentDto departmentDto) {
        Department department = new Department();
        department.setId(departmentDto.getId());
        department.setDepartmentName(departmentDto.getName());
        department.setFloorNumber(departmentDto.getFloorNumber());
        return department;
    }
}