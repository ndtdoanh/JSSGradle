package com.ndtdoanh.JSSGradle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndtdoanh.JSSGradle.util.SecurityUtil;
import com.ndtdoanh.JSSGradle.util.constant.LevelEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "jobs")
@Getter
@Setter
public class Job {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotBlank(message = "name không được để trống")
  private String name;

  @NotBlank(message = "location không được để trống")
  private String location;

  private double salary;
  private int quantity;
  private LevelEnum level;

  @Column(columnDefinition = "MEDIUMTEXT")
  private String description;

  private Instant startDate;
  private Instant endDate;
  private boolean active;
  private Instant createdAt;
  private Instant updatedAt;
  private String createdBy;
  private String updatedBy;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;

  @ManyToMany(fetch = FetchType.LAZY)
  @JsonIgnoreProperties(value = {"jobs"})
  @JoinTable(
      name = "job_skill",
      joinColumns = @JoinColumn(name = "job_id"),
      inverseJoinColumns = @JoinColumn(name = "skill_id"))
  private List<Skill> skills;

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
