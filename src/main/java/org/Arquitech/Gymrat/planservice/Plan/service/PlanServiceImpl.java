package org.Arquitech.Gymrat.planservice.Plan.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.Arquitech.Gymrat.planservice.Plan.domain.model.entity.Plan;
import org.Arquitech.Gymrat.planservice.Plan.domain.persistence.PlanRepository;
import org.Arquitech.Gymrat.planservice.Plan.domain.service.PlanService;
import org.Arquitech.Gymrat.planservice.Shared.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PlanServiceImpl implements PlanService {
    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private Validator validator;

    public PlanServiceImpl(PlanRepository planRepository, Validator validator){
        this.planRepository = planRepository;
        this.validator = validator;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Plan> fetchAll() {
        return planRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Plan> fetchById(Integer Id) {
        if (planRepository.existsById(Id)) {
            return planRepository.findById(Id);
        } else {
            throw new CustomException("Plan not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Plan save(Plan plan) {
        Set<ConstraintViolation<Plan>> violations = validator.validate(plan);
        if (!violations.isEmpty()) {
            throw new CustomException("Error", HttpStatus.NOT_FOUND);
        }
        return planRepository.save(plan);
    }

    @Override
    public Plan update(Plan plan) {
        Set<ConstraintViolation<Plan>> violations = validator.validate(plan);
        if (!violations.isEmpty()) {
            throw new CustomException("Error", HttpStatus.NOT_FOUND);
        }

        return planRepository
                .findById(plan.getId())
                .map(planToUpdate -> {
                    planToUpdate.setName(plan.getName());
                    planToUpdate.setDescription(plan.getDescription());
                    planToUpdate.setCost(plan.getCost());
                    planToUpdate.setDuration(plan.getDuration());

                    return planRepository.save(planToUpdate);
                })
                .orElseThrow(() -> new CustomException("Plan not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public boolean deleteById(Integer id) {
        var planToDelete = planRepository.findById(id)
                .orElseThrow(() -> new CustomException("Plan not found", HttpStatus.NOT_FOUND));

        planRepository.delete(planToDelete);
        return true;
    }
}
