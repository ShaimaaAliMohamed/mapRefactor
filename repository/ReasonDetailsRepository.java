package com.sadeem.smap.repository;

import com.sadeem.smap.model.ReasonDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReasonDetailsRepository extends JpaRepository<ReasonDetails, Long> {

    @Query("SELECT rd FROM ReasonDetails rd WHERE rd.isDeleted = false")
    List<ReasonDetails> findAllByIsDeletedFalse();

    @Query("SELECT rd FROM ReasonDetails rd WHERE rd.isDeleted = false")
    Page<ReasonDetails> findAllByIsDeletedFalse(Pageable pageable);
}