package com.sadeem.smap.service;

import com.sadeem.smap.dto.JobDto;
import com.sadeem.smap.model.Job;
import com.sadeem.smap.repository.JobRepository;
import com.sadeem.smap.util.EventResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public Iterable<JobDto> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<JobDto> getJobsPaginated(Pageable pageable) {
        return jobRepository.findAll(pageable).map(this::convertToDto);
    }

    public Optional<JobDto> getJobById(Long id) {
        return jobRepository.findById(id).map(this::convertToDto);
    }

    public EventResult createJob(JobDto jobDto) {
        Job job = convertToEntity(jobDto);
        jobRepository.save(job);
        return new EventResult("success", "Job created successfully");
    }

    public EventResult updateJob(JobDto jobDto) {
        Optional<Job> optionalJob = jobRepository.findById(jobDto.getId());
        if (optionalJob.isPresent()) {
            Job job = optionalJob.get();
            job.setJobName(jobDto.getName());
            job.setJobDescription(jobDto.getDescription());
            jobRepository.save(job);
            return new EventResult("success", "Job updated successfully");
        }
        return new EventResult("error", "Job not found");
    }

    public EventResult deleteJob(Long id) {
        jobRepository.deleteById(id);
        return new EventResult("success", "Job deleted successfully");
    }

    public void deleteAllJobs() {
        jobRepository.deleteAll();
    }

    private JobDto convertToDto(Job job) {
        JobDto dto = new JobDto();
        dto.setId(job.getId());
        dto.setName(job.getJobName());
        dto.setDescription(job.getJobDescription());
        return dto;
    }

    private Job convertToEntity(JobDto jobDto) {
        Job job = new Job();
        job.setId(jobDto.getId());
        job.setJobName(jobDto.getName());
        job.setJobDescription(jobDto.getDescription());
        return job;
    }
}