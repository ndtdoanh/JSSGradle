package com.ndtdoanh.JSSGradle.repository;

import com.ndtdoanh.JSSGradle.domain.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository
    extends JpaRepository<Resume, Long>, JpaSpecificationExecutor<Resume> {}
