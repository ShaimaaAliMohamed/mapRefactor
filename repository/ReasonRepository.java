package com.sadeem.smap.repository;

import com.sadeem.smap.model.Reason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReasonRepository extends JpaRepository<Reason, Long> {

    @Query("SELECT r FROM Reason r WHERE r.isDeleted = false")
    List<Reason> findAllByIsDeletedFalse();

    @Query("SELECT r FROM Reason r WHERE r.isDeleted = false")
    Page<Reason> findAllByIsDeletedFalse(Pageable pageable);
}