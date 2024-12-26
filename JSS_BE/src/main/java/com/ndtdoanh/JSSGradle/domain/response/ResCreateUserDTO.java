package com.ndtdoanh.JSSGradle.domain.response;

import com.ndtdoanh.JSSGradle.util.constant.GenderEnum;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateUserDTO {
  private long id;
  private String name;
  private String email;
  private GenderEnum gender;
  private String address;
  private int age;
  private Instant createdAt;
  private CompanyUser company;

  @Getter
  @Setter
  public static class CompanyUser {
    private long id;
    private String name;
  }
}
