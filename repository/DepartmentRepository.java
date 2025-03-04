package com.sadeem.smap.repository;

import com.sadeem.smap.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}