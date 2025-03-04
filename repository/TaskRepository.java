package com.sadeem.smap.repository;

import com.sadeem.smap.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}