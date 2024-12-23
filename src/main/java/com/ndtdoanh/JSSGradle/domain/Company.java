package com.ndtdoanh.JSSGradle.domain;

import com.ndtdoanh.JSSGradle.util.SecurityUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "companies")
public class Company {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotBlank(message = "name không được để trống")
  private String name;

  @Column(columnDefinition = "MEDIUMTEXT")
  private String description;

  private String address;
  private String logo;

  //  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
  private Instant createdAt;

  private Instant updatedAt;
  private String createdBy;
  private String updatedBy;

  @PrePersist
  public void handleBeforeCreate() {
    this.createdBy =
        SecurityUtil.getCurrentUserLogin().isPresent() == true
            ? SecurityUtil.getCurrentUserLogin().get()
            : "";
    this.createdAt = Instant.now();
  }

  @PreUpdate
  public void handleBeforeUpdate() {
    this.updatedBy =
        SecurityUtil.getCurrentUserLogin().isPresent() == true
            ? SecurityUtil.getCurrentUserLogin().get()
            : "";
    this.updatedAt = Instant.now();
  }
}
