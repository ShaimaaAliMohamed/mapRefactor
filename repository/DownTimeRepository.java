package com.sadeem.smap.repository;

import com.sadeem.smap.model.DownTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DownTimeRepository extends JpaRepository<DownTime, Long> {

    @Query("SELECT d FROM DownTime d WHERE YEAR(d.date) = :year AND MONTH(d.date) = :month")
    List<DownTime> findByMonth(int year, int month);

    @Query("SELECT d FROM DownTime d WHERE d.machine.department.id = :departmentId")
    List<DownTime> findByDepartmentId(Long departmentId);
}