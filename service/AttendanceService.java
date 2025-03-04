package com.sadeem.smap.service;

import com.sadeem.smap.dto.AttendanceDto;
import com.sadeem.smap.model.Attendance;
import com.sadeem.smap.model.Overtime;
import com.sadeem.smap.model.Permission;
import com.sadeem.smap.model.Vacation;
import com.sadeem.smap.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private OvertimeRepository overtimeRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private VacationRepository vacationRepository;

    @Autowired
    private WorkerRepository workerRepository;

    public String getAttendanceData(String departmentId, YearMonth yearMonth) {
        Map<Long, AttendanceDto> result = new HashMap<>();
        List<Worker> workers = departmentId != null ? workerRepository.findByDepartmentId(departmentId) : workerRepository.findAll();

        for (Worker worker : workers) {
            AttendanceDto dto = new AttendanceDto(worker.getName(), worker.getDepartment().getName(), new double[33]);
            result.put(worker.getId(), dto);
        }

        List<Attendance> attendances = attendanceRepository.findByYearAndMonth(yearMonth.getYear(), yearMonth.getMonthValue());
        for (Attendance attendance : attendances) {
            AttendanceDto dto = result.get(attendance.getWorker().getId());
            if (dto != null) {
                int dayOfMonth = attendance.getAbsentDate().getDayOfMonth();
                dto.setDay(dayOfMonth, 1); // Mark as absent
                dto.setTotalAbsences(dto.getTotalAbsences() + 1);
            }
        }

        List<Permission> permissions = permissionRepository.findByYearAndMonth(yearMonth.getYear(), yearMonth.getMonthValue());
        for (Permission permission : permissions) {
            AttendanceDto dto = result.get(permission.getWorker().getId());
            if (dto != null) {
                int dayOfMonth = permission.getPermissionDate().getDayOfMonth();
                dto.setDay(dayOfMonth, 0.5); // Mark as half-day
            }
        }

        return result.values().toString(); // Replace with proper JSON serialization
    }

    public String getVacationData(String departmentId, YearMonth yearMonth) {
        // Similar logic as above for vacations
        return "";
    }

    public String getOvertimeData(String departmentId, YearMonth yearMonth) {
        // Similar logic as above for overtime
        return "";
    }

    public void saveAttendance(String workerId, String date) {
        LocalDate absentDate = LocalDate.parse(date);
        Attendance attendance = attendanceRepository.findByWorkerIdAndDate(workerId, absentDate);
        if (attendance == null) {
            attendance = new Attendance();
            attendance.setWorker(new Worker(Long.parseLong(workerId)));
            attendance.setAbsentDate(absentDate);
        }
        attendanceRepository.save(attendance);
    }

    public void saveOvertime(String workerId, int hours, String date) {
        LocalDate overtimeDate = LocalDate.parse(date);
        Overtime overtime = overtimeRepository.findByWorkerIdAndDate(workerId, overtimeDate);
        if (overtime == null) {
            overtime = new Overtime();
            overtime.setWorker(new Worker(Long.parseLong(workerId)));
            overtime.setOvertimeHours(hours);
            overtime.setOvertimeDate(overtimeDate);
        }
        overtimeRepository.save(overtime);
    }

    public void savePermission(String workerId, String date) {
        LocalDate permissionDate = LocalDate.parse(date);
        Permission permission = permissionRepository.findByWorkerIdAndDate(workerId, permissionDate);
        if (permission == null) {
            permission = new Permission();
            permission.setWorker(new Worker(Long.parseLong(workerId)));
            permission.setPermissionDate(permissionDate);
        }
        permissionRepository.save(permission);
    }

    public void saveVacation(String workerId, String date) {
        LocalDate vacationDate = LocalDate.parse(date);
        Vacation vacation = vacationRepository.findByWorkerIdAndDate(workerId, vacationDate);
        if (vacation == null) {
            vacation = new Vacation();
            vacation.setWorker(new Worker(Long.parseLong(workerId)));
            vacation.setVacationDate(vacationDate);
        }
        vacationRepository.save(vacation);
    }
}