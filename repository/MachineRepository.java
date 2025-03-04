package com.sadeem.smap.repository;

import com.sadeem.smap.model.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineRepository extends JpaRepository<Machine, Long> {
}