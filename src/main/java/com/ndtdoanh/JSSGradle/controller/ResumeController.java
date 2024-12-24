package com.ndtdoanh.JSSGradle.controller;

import com.ndtdoanh.JSSGradle.domain.Resume;
import com.ndtdoanh.JSSGradle.domain.response.ResultPaginationDTO;
import com.ndtdoanh.JSSGradle.domain.response.resume.ResCreateResumeDTO;
import com.ndtdoanh.JSSGradle.domain.response.resume.ResFetchResumeDTO;
import com.ndtdoanh.JSSGradle.domain.response.resume.ResUpdateResumeDTO;
import com.ndtdoanh.JSSGradle.service.ResumeService;
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
@RequestMapping("/api/v1")
public class ResumeController {
  private final ResumeService resumeService;

  public ResumeController(ResumeService resumeService) {
    this.resumeService = resumeService;
  }

  @PostMapping("/resumes")
  @ApiMessage("Create a resume")
  public ResponseEntity<ResCreateResumeDTO> create(@Valid @RequestBody Resume resume)
      throws IdInvalidException {
    // check id exists
    boolean isIdExist = this.resumeService.checkResumeExistByUserAndJob(resume);
    if (!isIdExist) {
      throw new IdInvalidException("User id/Job id không tồn tại");
    }
    // create new resume
    return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.create(resume));
  }

  @PutMapping("/resumes")
  @ApiMessage("Update a resume")
  public ResponseEntity<ResUpdateResumeDTO> update(@RequestBody Resume resume)
      throws IdInvalidException {
    // check id exist
    Optional<Resume> reqResumeOptional = this.resumeService.fetchById(resume.getId());
    if (reqResumeOptional.isEmpty()) {
      throw new IdInvalidException("Resume với id = " + resume.getId() + " không tồn tại");
    }
    Resume reqResume = reqResumeOptional.get();
    reqResume.setStatus(resume.getStatus());
    return ResponseEntity.ok().body(this.resumeService.update(reqResume));
  }

  @DeleteMapping("/resumes/{id}")
  @ApiMessage("Delete a resume by id")
  public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
    Optional<Resume> reqResumeOptional = this.resumeService.fetchById(id);
    if (reqResumeOptional.isEmpty()) {
      throw new IdInvalidException("Resume với id = " + id + " không tồn tại");
    }
    this.resumeService.delete(id);
    return ResponseEntity.ok().body(null);
  }

  @GetMapping("/resumes/{id}")
  @ApiMessage("Fetch a resume by id")
  public ResponseEntity<ResFetchResumeDTO> fetchById(@PathVariable("id") long id)
      throws IdInvalidException {
    Optional<Resume> reqResumeOptional = this.resumeService.fetchById(id);
    if (reqResumeOptional.isEmpty()) {
      throw new IdInvalidException("Resume với id = " + id + " không tồn tại");
    }
    return ResponseEntity.ok().body(this.resumeService.getResume(reqResumeOptional.get()));
  }

  @GetMapping("/resumes")
  @ApiMessage("Fetch all resume with paginate")
  public ResponseEntity<ResultPaginationDTO> fetchAll(
      @Filter Specification<Resume> spec, Pageable pageable) {
    return ResponseEntity.ok().body(this.resumeService.fetchAllResume(spec, pageable));
  }
}
