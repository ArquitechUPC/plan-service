package org.Arquitech.Gymrat.planservice.Plan.resource.plan;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
public class CreatePlanResource {
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Double cost;
    @NotNull
    private Integer duration;
}
