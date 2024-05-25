package org.Arquitech.Gymrat.planservice.Plan.domain.persistence;

import org.Arquitech.Gymrat.planservice.Plan.domain.model.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer> {
    List<Plan> findAllByDuration(Integer duration);
}
