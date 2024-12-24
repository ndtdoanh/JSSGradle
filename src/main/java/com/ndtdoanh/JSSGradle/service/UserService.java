package com.ndtdoanh.JSSGradle.service;

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

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User handleCreateUser(User user) {
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
            .map(
                item ->
                    new ResUserDTO(
                        item.getId(),
                        item.getEmail(),
                        item.getName(),
                        item.getGender(),
                        item.getAddress(),
                        item.getAge(),
                        item.getUpdatedAt(),
                        item.getCreatedAt()))
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
    res.setId(user.getId());
    res.setEmail(user.getEmail());
    res.setName(user.getName());
    res.setAge(user.getAge());
    res.setCreatedAt(user.getCreatedAt());
    res.setGender(user.getGender());
    res.setAddress(user.getAddress());
    return res;
  }

  public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
    ResUpdateUserDTO res = new ResUpdateUserDTO();
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
