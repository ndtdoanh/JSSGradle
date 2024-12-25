package com.ndtdoanh.JSSGradle.service;

import com.ndtdoanh.JSSGradle.domain.Company;
import com.ndtdoanh.JSSGradle.domain.Role;
import com.ndtdoanh.JSSGradle.domain.User;
import com.ndtdoanh.JSSGradle.domain.response.ResCreateUserDTO;
import com.ndtdoanh.JSSGradle.domain.response.ResUpdateUserDTO;
import com.ndtdoanh.JSSGradle.domain.response.ResUserDTO;
import com.ndtdoanh.JSSGradle.domain.response.ResultPaginationDTO;
import com.ndtdoanh.JSSGradle.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final CompanyService companyService;
  private final RoleService roleService;

  public UserService(
      UserRepository userRepository, CompanyService companyService, RoleService roleService) {
    this.userRepository = userRepository;
    this.companyService = companyService;
    this.roleService = roleService;
  }

  public User handleCreateUser(User user) {

    // check company
    if (user.getCompany() != null) {
      Optional<Company> companyOptional = this.companyService.findById(user.getCompany().getId());
      user.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
    }

    // check role
    if (user.getRole() != null) {
      Role r = this.roleService.fetchById(user.getRole().getId());
      user.setRole(r != null ? r : null);
    }

    return this.userRepository.save(user);
  }

  public void handleDeleteUser(long id) {
    this.userRepository.deleteById(id);
  }

  public User fetchUserById(long id) {
    Optional<User> userOptional = this.userRepository.findById(id);
    if (userOptional.isPresent()) {
      return userOptional.get();
    }
    return null;
  }

  public ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {
    Page<User> pageUser = this.userRepository.findAll(spec, pageable);
    ResultPaginationDTO rs = new ResultPaginationDTO();
    ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

    mt.setPage(pageable.getPageNumber() + 1);
    mt.setPageSize(pageable.getPageSize());

    mt.setPages(pageUser.getTotalPages());
    mt.setTotal(pageUser.getTotalElements());

    rs.setMeta(mt);

    // remove sensitive data
    List<ResUserDTO> listUser =
        pageUser.getContent().stream()
            .map(item -> this.convertToResUserDTO(item))
            .collect(Collectors.toList());

    rs.setResult(listUser);

    return rs;
  }

  public User handleUpdateUser(User reqUser) {
    User currentUser = this.fetchUserById(reqUser.getId());
    if (currentUser != null) {
      currentUser.setAddress(reqUser.getAddress());
      currentUser.setGender(reqUser.getGender());
      currentUser.setAge(reqUser.getAge());
      currentUser.setName(reqUser.getName());

      // check company
      if (reqUser.getCompany() != null) {
        Optional<Company> companyOptional =
            this.companyService.findById(reqUser.getCompany().getId());
        currentUser.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
      }

      // check role
      if (reqUser.getRole() != null) {
        Role r = this.roleService.fetchById(reqUser.getRole().getId());
        currentUser.setRole(r != null ? r : null);
      }

      // update
      currentUser = this.userRepository.save(currentUser);
    }
    return currentUser;
  }

  public User handleGetUserByUsername(String username) {
    return this.userRepository.findByEmail(username);
  }

  public boolean isEmailExist(String email) {
    return this.userRepository.existsByEmail(email);
  }

  public ResCreateUserDTO convertToResCreateUserDTO(User user) {
    ResCreateUserDTO res = new ResCreateUserDTO();
    ResCreateUserDTO.CompanyUser com = new ResCreateUserDTO.CompanyUser();

    res.setId(user.getId());
    res.setEmail(user.getEmail());
    res.setName(user.getName());
    res.setAge(user.getAge());
    res.setCreatedAt(user.getCreatedAt());
    res.setGender(user.getGender());
    res.setAddress(user.getAddress());

    if (user.getCompany() != null) {
      com.setId(user.getCompany().getId());
      com.setName(user.getCompany().getName());
      res.setCompany(com);
    }
    return res;
  }

  public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
    ResUpdateUserDTO res = new ResUpdateUserDTO();
    ResUpdateUserDTO.CompanyUser com = new ResUpdateUserDTO.CompanyUser();

    if (user.getCompany() != null) {
      com.setId(user.getCompany().getId());
      com.setName(user.getCompany().getName());
      res.setCompany(com);
    }
    res.setId(user.getId());
    res.setName(user.getName());
    res.setAge(user.getAge());
    res.setUpdatedAt(user.getUpdatedAt());
    res.setGender(user.getGender());
    res.setAddress(user.getAddress());
    return res;
  }

  public ResUserDTO convertToResUserDTO(User user) {
    ResUserDTO res = new ResUserDTO();
    ResUserDTO.CompanyUser com = new ResUserDTO.CompanyUser();
    ResUserDTO.RoleUser roleUser = new ResUserDTO.RoleUser();

    if (user.getCompany() != null) {
      com.setId(user.getCompany().getId());
      com.setName(user.getCompany().getName());
      res.setCompany(com);
    }

    if (user.getRole() != null) {
      roleUser.setId(user.getRole().getId());
      roleUser.setName(user.getRole().getName());
      res.setRole(roleUser);
    }

    res.setId(user.getId());
    res.setEmail(user.getEmail());
    res.setName(user.getName());
    res.setAge(user.getAge());
    res.setUpdatedAt(user.getUpdatedAt());
    res.setCreatedAt(user.getCreatedAt());
    res.setGender(user.getGender());
    res.setAddress(user.getAddress());
    return res;
  }

  public void updateUserToken(String token, String email) {
    User currentUser = this.handleGetUserByUsername(email);
    if (currentUser != null) {
      currentUser.setRefreshToken(token);
      this.userRepository.save(currentUser);
    }
  }

  public User getUserByRefreshTokenAndEmail(String token, String email) {
    return this.userRepository.findByRefreshTokenAndEmail(token, email);
  }
}
