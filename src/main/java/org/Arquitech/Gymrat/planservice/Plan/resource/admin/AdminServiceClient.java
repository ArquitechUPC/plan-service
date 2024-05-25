package org.Arquitech.Gymrat.planservice.Plan.resource.admin;

import org.Arquitech.Gymrat.planservice.Plan.resource.payment.PaymentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "admin-service", url = "http://localhost:8082")
@Component
public interface AdminServiceClient {

    @PostMapping("/api/v1/admin/payments")
    void notifyPayment(@RequestBody PaymentDto paymentDto);
}