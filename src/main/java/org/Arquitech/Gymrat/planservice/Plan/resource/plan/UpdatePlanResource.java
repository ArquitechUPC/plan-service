package org.Arquitech.Gymrat.planservice.Plan.resource.plan;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePlanResource {

    @NotNull
    @NotBlank
    @Min(1)
    private Integer Id;

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String description;

    @NotBlank
    @NotNull
    private Double cost;

    @NotBlank
    @NotNull
    private Integer duration;
}
