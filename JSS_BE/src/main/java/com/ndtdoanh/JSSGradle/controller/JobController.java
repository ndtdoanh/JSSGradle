package com.ndtdoanh.JSSGradle.controller;

import com.ndtdoanh.JSSGradle.domain.Job;
import com.ndtdoanh.JSSGradle.domain.response.ResultPaginationDTO;
import com.ndtdoanh.JSSGradle.domain.response.job.ResCreateJobDTO;
import com.ndtdoanh.JSSGradle.domain.response.job.ResUpdateJobDTO;
import com.ndtdoanh.JSSGradle.service.JobService;
import com.ndtdoanh.JSSGradle.util.annotation.ApiMessage;
import com.ndtdoanh.JSSGradle.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class JobController {
  private final JobService jobService;

  public JobController(JobService jobService) {
    this.jobService = jobService;
  }

  @PostMapping("/jobs")
  @ApiMessage("Create a job")
  public ResponseEntity<ResCreateJobDTO> create(@Valid @RequestBody Job job) {
    return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.create(job));
  }

  @PutMapping("/jobs")
  @ApiMessage("Update a job")
  public ResponseEntity<ResUpdateJobDTO> update(@Valid @RequestBody Job job)
      throws IdInvalidException {
    Optional<Job> currentJob = this.jobService.fetchJobById(job.getId());
    if (!currentJob.isPresent()) {
      throw new IdInvalidException("Job not found");
    }
    return ResponseEntity.ok().body(this.jobService.update(job, currentJob.get()));
  }

  @DeleteMapping("/jobs/{id}")
  @ApiMessage("Delete a job by id")
  public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
    Optional<Job> currentJob = this.jobService.fetchJobById(id);
    if (!currentJob.isPresent()) {
      throw new IdInvalidException("Job not found");
    }
    this.jobService.delete(id);
    return ResponseEntity.ok().body(null);
  }

  @GetMapping("/jobs/{id}")
  @ApiMessage("Get a job by id")
  public ResponseEntity<Job> getJob(@PathVariable("id") long id) throws IdInvalidException {
    Optional<Job> currentJob = this.jobService.fetchJobById(id);
    if (!currentJob.isPresent()) {
      throw new IdInvalidException("Job not found");
    }
    return ResponseEntity.ok().body(currentJob.get());
  }

  @GetMapping("/jobs")
  @ApiMessage("Get job with pagination")
  public ResponseEntity<ResultPaginationDTO> getAllJob(
      @Filter Specification<Job> spec, Pageable pageable) {
    return ResponseEntity.ok().body(this.jobService.fetchAll(spec, pageable));
  }
}