package com.sadeem.smap.repository;

import com.sadeem.smap.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByYearAndMonth(int year, int month);
    Attendance findByWorkerIdAndDate(String workerId, LocalDate date);
}