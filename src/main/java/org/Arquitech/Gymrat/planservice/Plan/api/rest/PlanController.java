package org.Arquitech.Gymrat.planservice.Plan.api.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.Arquitech.Gymrat.planservice.Plan.domain.model.entity.Plan;
import org.Arquitech.Gymrat.planservice.Plan.domain.service.PlanService;
import org.Arquitech.Gymrat.planservice.Plan.mapping.PlanMapper;
import org.Arquitech.Gymrat.planservice.Plan.resource.plan.CreatePlanResource;
import org.Arquitech.Gymrat.planservice.Plan.resource.plan.PlanResource;
import org.Arquitech.Gymrat.planservice.Plan.resource.plan.UpdatePlanResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Plans", description = "Create, Read, Update and delete plans entities")
@RestController
@RequestMapping("api/v1/plans")
@AllArgsConstructor
public class PlanController {

    private final PlanService planService;
    private final PlanMapper mapper;

    @Operation(summary = "Get all registered plans", responses = {
            @ApiResponse(description = "Successfully fetched all plans",
            responseCode = "201",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = PlanResource.class)))
    })
    @GetMapping
    public List<Plan> fetchAll() {return planService.fetchAll();}

    @Operation(summary = "Get a plan by id", responses = {
            @ApiResponse(description = "Successfully fetched plan by id",
                    responseCode = "201",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlanResource.class)))
    })
    @GetMapping("{id}")
    public PlanResource fetchById(@PathVariable Integer id){
        return this.mapper.toResource(planService.fetchById(id).get());
    }

    @Operation(summary = "Save a plan", responses = {
            @ApiResponse(description = "Plan successfully created",
                    responseCode = "201",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlanResource.class)))
    })
    @PostMapping
    public PlanResource save(@RequestBody CreatePlanResource resource){
        return this.mapper.toResource(planService.save(this.mapper.toModel(resource)));
    }

        @Operation(summary = "Update a plan by id", responses = {
                @ApiResponse(description = "Plan successfully updated",
                        responseCode = "201",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = PlanResource.class)))
        })
        @PutMapping("{id}")
        public ResponseEntity<PlanResource> update(@PathVariable Integer id, @RequestBody UpdatePlanResource resource){
            if(id.equals(resource.getId())) {
              PlanResource planResource = mapper.toResource(
                      planService.update(mapper.toModel(resource)));
              return new ResponseEntity<>(planResource, HttpStatus.OK);
            }
            else {
                return ResponseEntity.badRequest().build();
            }
        }

    @Operation(summary = "Delete a plan by id", responses = {
            @ApiResponse(description = "Successfully deleted plan by id",
                    responseCode = "201",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlanResource.class)))
    })
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        if(planService.deleteById(id)){
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }


}
