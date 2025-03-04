package com.sadeem.smap.controller;

import com.sadeem.smap.dto.DepartmentTasksSheet;
import com.sadeem.smap.dto.OrderProfile;
import com.sadeem.smap.dto.WorkerPerformanceDto;
import com.sadeem.smap.service.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    @Autowired
    private ReportsService reportsService;

    /**
     * Get department process report.
     */
    @GetMapping("/department-process")
    public List<DepartmentTasksSheet> getDepartmentProcess(@RequestParam(required = false) Integer departmentId, Locale locale) {
        return reportsService.getDepartmentProcess(departmentId);
    }

    /**
     * Get order profile report.
     */
    @GetMapping("/order-profile")
    public OrderProfile getOrderProfile(@RequestParam Integer orderId, Locale locale) {
        return reportsService.getOrderProfile(orderId);
    }

    /**
     * Get worker performance report.
     */
    @GetMapping("/worker-performance")
    public List<WorkerPerformanceDto> getWorkerPerformance(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            Locale locale) {
        return reportsService.getWorkerPerformance(startDate, endDate);
    }

    /**
     * Get machine power data.
     */
    @GetMapping("/machine-power")
    public Object getMachinePower(
            @RequestParam String machineId,
            @RequestParam String start,
            Locale locale) {
        return reportsService.getMachinePower(machineId, start);
    }

    /**
     * Get worker task event failures.
     */
    @GetMapping("/worker-task-failures")
    public List<?> getWorkerTaskFailures(
            @RequestParam String workerId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            Locale locale) {
        return reportsService.getWorkerTaskFailures(workerId, startDate, endDate);
    }
}