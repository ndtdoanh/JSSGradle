package com.ndtdoanh.JSSGradle.domain.response;

import com.ndtdoanh.JSSGradle.util.constant.GenderEnum;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateUserDTO {
  private long id;
  private String name;
  private GenderEnum gender;
  private String address;
  private int age;
  private Instant updatedAt;
}
