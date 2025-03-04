package com.sadeem.smap.service;

import com.sadeem.smap.dto.*;
import com.sadeem.smap.model.*;
import com.sadeem.smap.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportsService {

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private TaskCreateRepository taskCreateRepository;

    @Autowired
    private TaskRunTimeRepository taskRunTimeRepository;

    @Autowired
    private TaskWorkerRepository taskWorkerRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private MachinePowerRepository machinePowerRepository;

    /**
     * Generate department process report.
     */
    public List<DepartmentTasksSheet> getDepartmentProcess(Integer departmentId) {
        List<Process> processes = processRepository.findByDepartmentId(departmentId);
        Map<Integer, DepartmentTasksSheet> processMap = new HashMap<>();

        for (Process process : processes) {
            int complete