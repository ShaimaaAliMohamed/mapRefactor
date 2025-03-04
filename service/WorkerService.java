package com.sadeem.smap.service;

import com.sadeem.smap.dto.WorkerDto;
import com.sadeem.smap.model.Department;
import com.sadeem.smap.model.Employee;
import com.sadeem.smap.model.Group;
import com.sadeem.smap.model.Job;
import com.sadeem.smap.model.Skill;
import com.sadeem.smap.model.Worker;
import com.sadeem.smap.repository.DepartmentRepository;
import com.sadeem.smap.repository.GroupRepository;
import com.sadeem.smap.repository.JobRepository;
import com.sadeem.smap.repository.SkillRepository;
import com.sadeem.smap.repository.WorkerRepository;
import com.sadeem.smap.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WorkerService {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private FileUtil fileUtil;

    public Iterable<WorkerDto> getAllWorkers() {
        return workerRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<WorkerDto> getWorkersPaginated(Pageable pageable) {
        return workerRepository.findAll(pageable).map(this::convertToDto);
    }

    public Optional<WorkerDto> getWorkerById(Long id) {
        return workerRepository.findById(id).map(this::convertToDto);
    }

    public void createWorker(WorkerDto workerDto, MultipartFile file) throws IOException {
        Worker worker = convertToEntity(workerDto);
        workerRepository.save(worker);
        fileUtil.writeToFile(file, "/opt/images/employees/" + worker.getEmployee().getEmployeeId() + ".jpg");
    }

    public void updateWorker(WorkerDto workerDto, MultipartFile file) throws IOException {
        Optional<Worker> optionalWorker = workerRepository.findById(workerDto.getId());
        if (optionalWorker.isPresent()) {
            Worker worker = optionalWorker.get();
            Employee employee = worker.getEmployee();

            // Update employee details
            employee.setDepartment(departmentRepository.findById(workerDto.getDepartmentId()).orElseThrow());
            employee.setGroup(groupRepository.findById(workerDto.getGroupId()).orElseThrow());
            employee.setName(workerDto.getName());
            employee.setBirthdate(workerDto.getBirthDate());
            employee.setStartWorkDate(workerDto.getEmploymentDate());
            employee.setExperienceYears(workerDto.getExperience());

            // Update worker details
            worker.setTagId(workerDto.getTagId());

            // Update worker jobs
            Set<Job> jobs = workerDto.getJobIds().stream()
                    .map(jobId -> jobRepository.findById(jobId).orElseThrow())
                    .collect(Collectors.toSet());
            worker.getWorkerJobs().clear();
            worker.getWorkerJobs().addAll(jobs.stream()
                    .map(job -> new WorkerJob(worker, job))
                    .collect(Collectors.toSet()));

            // Update worker skills
            Set<Skill> skills = workerDto.getSkillIds().stream()
                    .map(skillId -> skillRepository.findById(skillId).orElseThrow())
                    .collect(Collectors.toSet());
            worker.getWorkerSkills().clear();
            worker.getWorkerSkills().addAll(skills.stream()
                    .map(skill -> new WorkerSkill(worker, skill))
                    .collect(Collectors.toSet()));

            workerRepository.save(worker);

            if (file != null && !file.isEmpty()) {
                fileUtil.writeToFile(file, "/opt/images/employees/" + worker.getEmployee().getEmployeeId() + ".jpg");
            }
        }
    }

    public void deleteWorker(Long id) {
        workerRepository.deleteById(id);
    }

    public void deleteAllWorkers() {
        workerRepository.deleteAll();
    }

    private WorkerDto convertToDto(Worker worker) {
        WorkerDto dto = new WorkerDto();
        dto.setId(worker.getId());
        dto.setName(worker.getEmployee().getName());
        dto.setBirthDate(worker.getEmployee().getBirthdate());
        dto.setEmploymentDate(worker.getEmployee().getStartWorkDate());
        dto.setExperience(worker.getEmployee().getExperienceYears());
        dto.setTagId(worker.getTagId());
        dto.setDepartmentId(worker.getEmployee().getDepartment().getId());
        dto.setGroupId(worker.getEmployee().getGroup().getId());
        dto.setJobIds(worker.getWorkerJobs().stream()
                .map(workerJob -> workerJob.getJob().getId())
                .collect(Collectors.toSet()));
        dto.setSkillIds(worker.getWorkerSkills().stream()
                .map(workerSkill -> workerSkill.getSkill().getId())
                .collect(Collectors.toSet()));
        return dto;
    }

    private Worker convertToEntity(WorkerDto workerDto) {
        Worker worker = new Worker();
        Employee employee = new Employee();
        employee.setDepartment(departmentRepository.findById(workerDto.getDepartmentId()).orElseThrow());
        employee.setGroup(groupRepository.findById(workerDto.getGroupId()).orElseThrow());
        employee.setName(workerDto.getName());
        employee.setBirthdate(workerDto.getBirthDate());
        employee.setStartWorkDate(workerDto.getEmploymentDate());
        employee.setExperienceYears(workerDto.getExperience());
        worker.setEmployee(employee);
        worker.setTagId(workerDto.getTagId());
        return worker;
    }
}