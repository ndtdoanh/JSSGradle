package com.ndtdoanh.JSSGradle.service;

import com.ndtdoanh.JSSGradle.domain.Company;
import com.ndtdoanh.JSSGradle.domain.User;
import com.ndtdoanh.JSSGradle.domain.response.ResultPaginationDTO;
import com.ndtdoanh.JSSGradle.repository.CompanyRepository;
import com.ndtdoanh.JSSGradle.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
  private final CompanyRepository companyRepository;
  private final UserRepository userRepository;

  public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
    this.companyRepository = companyRepository;
    this.userRepository = userRepository;
  }

  public Company handleCreateCompany(Company c) {
    return this.companyRepository.save(c);
  }

  public ResultPaginationDTO handleGetCompany(Specification<Company> spec, Pageable pageable) {
    Page<Company> pageCompany = this.companyRepository.findAll(spec, pageable);
    ResultPaginationDTO rs = new ResultPaginationDTO();
    ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

    mt.setPage(pageable.getPageNumber() + 1);
    mt.setPageSize(pageable.getPageSize());

    mt.setPages(pageCompany.getTotalPages());
    mt.setTotal(pageCompany.getTotalElements());

    rs.setMeta(mt);
    rs.setResult(pageCompany.getContent());

    return rs;
  }

  public Company handleUpdateCompany(Company c) {
    Optional<Company> companyOptional = this.companyRepository.findById(c.getId());
    if (companyOptional.isPresent()) {
      Company currentCompany = companyOptional.get();
      currentCompany.setLogo(c.getLogo());
      currentCompany.setName(c.getName());
      currentCompany.setDescription(c.getDescription());
      currentCompany.setAddress(c.getAddress());
      return this.companyRepository.save(currentCompany);
    }
    return null;
  }

  public void handleDeleteCompany(long id) {
    Optional<Company> comOptional = this.companyRepository.findById(id);
    if (comOptional.isPresent()) {
      Company com = comOptional.get();
      // fetch all user belong to this company
      List<User> users = this.userRepository.findByCompany(com);
      this.userRepository.deleteAll(users);
    }
    this.companyRepository.deleteById(id);
  }

  public Optional<Company> findById(long id) {
    return this.companyRepository.findById(id);
  }
}
