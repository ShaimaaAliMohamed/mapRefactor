package com.sadeem.smap.repository;

import com.sadeem.smap.model.Dimension;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DimensionRepository extends JpaRepository<Dimension, Long> {
}