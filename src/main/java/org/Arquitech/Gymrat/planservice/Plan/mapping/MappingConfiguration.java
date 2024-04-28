package org.Arquitech.Gymrat.planservice.Plan.mapping;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("clientMappingConfiguration")
public class MappingConfiguration {

    @Bean
    public PlanMapper planMapper(){ return new PlanMapper(); }


}
