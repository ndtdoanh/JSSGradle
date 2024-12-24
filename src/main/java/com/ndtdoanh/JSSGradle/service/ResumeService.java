package com.ndtdoanh.JSSGradle.service;

import com.ndtdoanh.JSSGradle.domain.Job;
import com.ndtdoanh.JSSGradle.domain.Resume;
import com.ndtdoanh.JSSGradle.domain.User;
import com.ndtdoanh.JSSGradle.domain.response.ResultPaginationDTO;
import com.ndtdoanh.JSSGradle.domain.response.resume.ResCreateResumeDTO;
import com.ndtdoanh.JSSGradle.domain.response.resume.ResFetchResumeDTO;
import com.ndtdoanh.JSSGradle.domain.response.resume.ResUpdateResumeDTO;
import com.ndtdoanh.JSSGradle.repository.JobRepository;
import com.ndtdoanh.JSSGradle.repository.ResumeRepository;
import com.ndtdoanh.JSSGradle.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ResumeService {
  private final ResumeRepository resumeRepository;
  private final UserRepository userRepository;
  private final JobRepository jobRepository;

  public ResumeService(
      ResumeRepository resumeRepository,
      UserRepository userRepository,
      JobRepository jobRepository) {
    this.resumeRepository = resumeRepository;
    this.userRepository = userRepository;
    this.jobRepository = jobRepository;
  }

  public Optional<Resume> fetchById(long id) {
    return this.resumeRepository.findById(id);
  }

  public boolean checkResumeExistByUserAndJob(Resume resume) {
    // check user by id
    if (resume.getUser() == null) return false;
    Optional<User> userOptional = this.userRepository.findById(resume.getUser().getId());
    if (userOptional.isEmpty()) return false;
    // check job by id
    if (resume.getJob() == null) return false;
    Optional<Job> jobOptional = this.jobRepository.findById(resume.getJob().getId());
    if (jobOptional.isEmpty()) return false;
    return true;
  }

  public ResCreateResumeDTO create(Resume resume) {
    resume = this.resumeRepository.save(resume);
    ResCreateResumeDTO res = new ResCreateResumeDTO();
    res.setId(resume.getId());
    res.setCreatedBy(resume.getCreatedBy());
    res.setCreatedAt(resume.getCreatedAt());
    return res;
  }

  public ResUpdateResumeDTO update(Resume resume) {
    resume = this.resumeRepository.save(resume);
    ResUpdateResumeDTO res = new ResUpdateResumeDTO();
    res.setUpdatedAt(resume.getUpdatedAt());
    res.setUpdatedBy(resume.getUpdatedBy());
    return res;
  }

  public void delete(long id) {
    this.resumeRepository.deleteById(id);
  }

  public ResFetchResumeDTO getResume(Resume resume) {
    ResFetchResumeDTO res = new ResFetchResumeDTO();
    res.setId(resume.getId());
    res.setEmail(resume.getEmail());
    res.setUrl(resume.getUrl());
    res.setStatus(resume.getStatus());
    res.setCreatedAt(resume.getCreatedAt());
    res.setCreatedBy(resume.getCreatedBy());
    res.setUpdatedAt(resume.getUpdatedAt());
    res.setUpdatedBy(resume.getUpdatedBy());

    if (resume.getJob() != null) {
      res.setCompanyName(resume.getJob().getCompany().getName());
    }

    res.setUser(
        new ResFetchResumeDTO.UserResume(resume.getUser().getId(), resume.getUser().getName()));
    res.setJob(new ResFetchResumeDTO.JobResume(resume.getJob().getId(), resume.getJob().getName()));
    return res;
  }

  public ResultPaginationDTO fetchAllResume(Specification<Resume> spec, Pageable pageable) {
    Page<Resume> pageUser = this.resumeRepository.findAll(spec, pageable);
    ResultPaginationDTO rs = new ResultPaginationDTO();
    ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
    mt.setPage(pageable.getPageNumber() + 1);
    mt.setPageSize(pageable.getPageSize());
    mt.setPages(pageUser.getTotalPages());
    mt.setTotal(pageUser.getTotalElements());
    rs.setMeta(mt);
    // remove sensitive data
    List<ResFetchResumeDTO> listResume =
        pageUser.getContent().stream()
            .map(item -> this.getResume(item))
            .collect(Collectors.toList());
    rs.setResult(listResume);
    return rs;
  }
}
