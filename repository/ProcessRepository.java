package com.sadeem.smap.repository;

import com.sadeem.smap.model.Process;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessRepository extends JpaRepository<Process, Long> {
}