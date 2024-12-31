package com.ndtdoanh.JSSGradle.domain.response;

import com.ndtdoanh.JSSGradle.util.constant.GenderEnum;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO {
  private long id;
  private String email;
  private String name;
  private GenderEnum gender;
  private String address;
  private int age;
  private Instant updatedAt;
  private Instant createdAt;
  private CompanyUser company;
  private RoleUser role;

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CompanyUser {
    private long id;
    private String name;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class RoleUser {
    private long id;
    private String name;
  }
}