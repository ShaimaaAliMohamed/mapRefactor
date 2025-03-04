package com.sadeem.smap.repository;

import com.sadeem.smap.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}