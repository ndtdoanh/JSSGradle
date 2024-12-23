package com.ndtdoanh.JSSGradle.repository;

import com.ndtdoanh.JSSGradle.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository
    extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {}
