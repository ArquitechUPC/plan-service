package org.Arquitech.Gymrat.planservice.Plan.resource.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDto {
    private Integer id;
    private String stripeCardToken;
    private String stripeCustomerId;
}
