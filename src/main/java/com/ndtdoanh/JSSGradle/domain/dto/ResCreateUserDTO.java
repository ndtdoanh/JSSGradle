package com.ndtdoanh.JSSGradle.domain.dto;

import lombok.Getter;
import lombok.Setter;
import com.ndtdoanh.JSSGradle.util.constant.GenderEnum;

import java.time.Instant;

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
}
