package com.sadeem.smap.service;

import com.sadeem.smap.dto.DownTimeReportDto;
import com.sadeem.smap.dto.ReasonHourDto;
import com.sadeem.smap.model.Department;
import com.sadeem.smap.model.Machine;
import com.sadeem.smap.model.DownTime;
import com.sadeem.smap.repository.*;
import com.sadeem.smap.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DownTimeService {

    @Autowired
    private DownTimeRepository downTimeRepository;

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ReasonDetailsRepository reasonDetailsRepository;

    @Autowired
    private DateUtil dateUtil;

    /**
     * Generate downtime report for machines in a specific department and month.
     */
    public List<DownTimeReportDto> getDownTimeReport(String departmentId, String month) {
        LocalDate parsedMonth = LocalDate.parse(month + "-01", DateUtil.DATE_FORMATTER);
        int year = parsedMonth.getYear();
        Month calendarMonth = parsedMonth.getMonth();

        List<Machine> machines = departmentId == null || departmentId.isEmpty()
                ? machineRepository.findAll()
                : machineRepository.findByDepartmentId(Long.parseLong(departmentId));

        Map<Long, DownTimeReportDto> machineMap = new HashMap<>();
        machines.forEach(machine -> machineMap.put(machine.getId(), new DownTimeReportDto(machine.getName(), new double[32])));

        List<DownTime> downTimes = downTimeRepository.findByMonth(year, calendarMonth.getValue());
        downTimes.forEach(downTime -> {
            DownTimeReportDto dto = machineMap.get(downTime.getMachine().getId());
            if (dto != null) {
                LocalDate downtimeDate = downTime.getDate();
                int dayOfMonth = downtimeDate.getDayOfMonth();
                if (dto.getMyNum()[dayOfMonth] == 0) {
                    dto.getMyNum()[dayOfMonth] += dateUtil.getWorkingHoursBetweenTwoDates(downTime.getStartDate(), downTime.getEndDate());
                    dto.getReasons()[dayOfMonth] = downTime.getReasonDetails().getName();
                }
            }
        });

        return new ArrayList<>(machineMap.values());
    }

    /**
     * Generate machine downtime report for a specific month.
     */
    public List<DownTimeReportDto> getMachineDownTimeReport(String month) {
        LocalDate parsedMonth = LocalDate.parse(month + "-01", DateUtil.DATE_FORMATTER);
        int year = parsedMonth.getYear();
        Month calendarMonth = parsedMonth.getMonth();

        List<Machine> machines = machineRepository.findAll();
        Map<Long, DownTimeReportDto> machineMap = new HashMap<>();
        machines.forEach(machine -> machineMap.put(machine.getId(), new DownTimeReportDto(machine.getName(), new double[32])));

        List<DownTime> downTimes = downTimeRepository.findByMonth(year, calendarMonth.getValue());
        downTimes.forEach(downTime -> {
            DownTimeReportDto dto = machineMap.get(downTime.getMachine().getId());
            if (dto != null) {
                LocalDate downtimeDate = downTime.getDate();
                int dayOfMonth = downtimeDate.getDayOfMonth();
                if (dto.getMyNum()[dayOfMonth] == 0) {
                    dto.getMyNum()[dayOfMonth] += dateUtil.getWorkingHoursBetweenTwoDates(downTime.getStartDate(), downTime.getEndDate());
                    dto.getReasons()[dayOfMonth] = downTime.getReasonDetails().getName();
                }
            }
        });

        return new ArrayList<>(machineMap.values());
    }

    /**
     * Generate worker downtime report for a specific month.
     */
    public List<DownTimeReportDto> getWorkerDownTimeReport(String month) {
        LocalDate parsedMonth = LocalDate.parse(month + "-01", DateUtil.DATE_FORMATTER);
        int year = parsedMonth.getYear();
        Month calendarMonth = parsedMonth.getMonth();

        List<DownTime> downTimes = downTimeRepository.findByMonth(year, calendarMonth.getValue());
        Map<Long, DownTimeReportDto> workerMap = new HashMap<>();

        downTimes.forEach(downTime -> {
            downTime.getTaskRuntime().getTaskWorkers().forEach(taskWorker -> {
                Long workerId = taskWorker.getWorker().getId();
                DownTimeReportDto dto = workerMap.computeIfAbsent(workerId, id -> new DownTimeReportDto(taskWorker.getWorker().getName(), new double[32]));
                LocalDate downtimeDate = downTime.getDate();
                int dayOfMonth = downtimeDate.getDayOfMonth();
                if (dto.getMyNum()[dayOfMonth] == 0) {
                    dto.getMyNum()[dayOfMonth] += dateUtil.getWorkingHoursBetweenTwoDates(downTime.getStartDate(), downTime.getEndDate());
                    dto.getReasons()[dayOfMonth] = downTime.getReasonDetails().getName();
                }
            });
        });

        return new ArrayList<>(workerMap.values());
    }

    /**
     * Mark downtime as solved.
     */
    public void markAsSolved(Long id) {
        Optional<DownTime> optionalDownTime = downTimeRepository.findById(id);
        if (optionalDownTime.isPresent()) {
            DownTime downTime = optionalDownTime.get();
            downTime.setStatus("SOLVED");
            downTimeRepository.save(downTime);
        }
    }

    /**
     * Mark downtime as seen.
     */
    public void markAsSeen(Long id) {
        Optional<DownTime> optionalDownTime = downTimeRepository.findById(id);
        if (optionalDownTime.isPresent()) {
            DownTime downTime = optionalDownTime.get();
            downTime.setStatus("SEEN");
            downTimeRepository.save(downTime);
        }
    }

    /**
     * Calculate reason hours, optionally filtered by department and date range.
     */
    public List<ReasonHourDto> calculateReasonHours(String startDate, String endDate, String departmentId) {
        List<ReasonHourDto> result = new ArrayList<>();
        List<DownTime> downTimes = departmentId == null || departmentId.isEmpty()
                ? downTimeRepository.findAll()
                : downTimeRepository.findByDepartmentId(Long.parseLong(departmentId));

        Map<Long, ReasonHourDto> reasonMap = new LinkedHashMap<>();
        downTimes.forEach(downTime -> {
            if (isWithinRange(downTime.getStartDate(), downTime.getEndDate(), startDate, endDate)) {
                Long reasonId = downTime.getReasonDetails().getId();
                ReasonHourDto dto = reasonMap.computeIfAbsent(reasonId, id -> new ReasonHourDto(downTime.getReasonDetails().getName(), 0.0));
                dto.setHours(dto.getHours() + dateUtil.getWorkingHoursBetweenTwoDates(downTime.getStartDate(), downTime.getEndDate()));
            }
        });

        return new ArrayList<>(reasonMap.values());
    }

    private boolean isWithinRange(LocalDate downtimeStart, LocalDate downtimeEnd, String startDate, String endDate) {
        LocalDate start = startDate == null || startDate.isEmpty() ? LocalDate.MIN : LocalDate.parse(startDate, DateUtil.DATE_FORMATTER);
        LocalDate end = endDate == null || endDate.isEmpty() ? LocalDate.MAX : LocalDate.parse(endDate, DateUtil.DATE_FORMATTER);

        return !downtimeStart.isAfter(end) && !downtimeEnd.isBefore(start);
    }
}