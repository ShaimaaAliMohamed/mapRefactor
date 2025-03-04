package com.sadeem.smap.repository;

import com.sadeem.smap.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, Long> {
}