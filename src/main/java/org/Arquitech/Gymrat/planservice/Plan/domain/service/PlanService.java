package org.Arquitech.Gymrat.planservice.Plan.domain.service;

import org.Arquitech.Gymrat.planservice.Plan.domain.model.entity.Plan;

import java.util.List;
import java.util.Optional;

public interface PlanService {
    List<Plan> fetchAll();
    Optional<Plan> fetchById(Integer Id);
    Plan save(Plan plan);
    Plan update(Plan plan);
    boolean deleteById(Integer id);
}
