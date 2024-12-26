package com.ndtdoanh.JSSGradle.repository;

import com.ndtdoanh.JSSGradle.domain.Skill;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository
    extends JpaRepository<Skill, Long>, JpaSpecificationExecutor<Skill> {
  boolean existsByName(String name);

  List<Skill> findByIdIn(List<Long> id);
}
