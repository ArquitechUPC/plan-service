package org.Arquitech.Gymrat.planservice.Plan.mapping;

import org.Arquitech.Gymrat.planservice.Plan.domain.model.entity.Plan;
import org.Arquitech.Gymrat.planservice.Plan.resource.plan.CreatePlanResource;
import org.Arquitech.Gymrat.planservice.Plan.resource.plan.PlanResource;
import org.Arquitech.Gymrat.planservice.Plan.resource.plan.UpdatePlanResource;
import org.Arquitech.Gymrat.planservice.Shared.mapping.EnhancedModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

public class PlanMapper implements Serializable {

    @Autowired
    EnhancedModelMapper mapper;
    public Plan toModel(CreatePlanResource resource){ return this.mapper.map(resource, Plan.class);}
    public Plan toModel(UpdatePlanResource resource){ return this.mapper.map(resource, Plan.class);}
    public PlanResource toResource(Plan plan){ return this.mapper.map(plan, PlanResource.class);}

}
