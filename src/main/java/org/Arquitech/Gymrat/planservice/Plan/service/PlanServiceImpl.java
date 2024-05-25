package org.Arquitech.Gymrat.planservice.Plan.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.Arquitech.Gymrat.planservice.Plan.domain.model.entity.Plan;
import org.Arquitech.Gymrat.planservice.Plan.domain.persistence.PlanRepository;
import org.Arquitech.Gymrat.planservice.Plan.domain.service.PlanService;
import org.Arquitech.Gymrat.planservice.Plan.resource.admin.AdminServiceClient;
import org.Arquitech.Gymrat.planservice.Plan.resource.client.ClientDto;
import org.Arquitech.Gymrat.planservice.Plan.resource.client.ClientServiceClient;
import org.Arquitech.Gymrat.planservice.Plan.resource.payment.PaymentDto;
import org.Arquitech.Gymrat.planservice.Shared.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PlanServiceImpl implements PlanService {
    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private AdminServiceClient adminServiceClient;

    @Autowired
    private ClientServiceClient clientServiceClient;

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

    @Override
    public Plan processSubscriptionPayment(Integer clientId, Integer planId, PaymentDto paymentDto) {

        ClientDto clientDto = clientServiceClient.getClientById(clientId);


        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new CustomException("Plan not found", HttpStatus.NOT_FOUND));


        try {

            String stripeCardToken = clientDto.getStripeCardToken();

            PaymentIntent paymentIntent = PaymentIntent.create(
                    new PaymentIntentCreateParams.Builder()
                            .setAmount(paymentDto.getAmount().multiply(new BigDecimal(100)).longValue())
                            .setCurrency("usd")
                            .setCustomer(clientDto.getStripeCustomerId())
                            .setPaymentMethod(stripeCardToken)
                            .build()
            );


            paymentDto.setStripeTransactionId(paymentIntent.getId());
            paymentDto.setPaymentStatus("paid");

        } catch (StripeException e) {

            throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        adminServiceClient.notifyPayment(paymentDto);

        return plan;
    }

    @Scheduled(cron = "0 0 1 * * *") // Ejecuta cada día a la 1:00 AM
    public void generateMonthlyFees() {
        List<Plan> monthlyPlans = planRepository.findAllByDuration(30);
        for (Plan plan : monthlyPlans) {

            BigDecimal cuota = new BigDecimal(plan.getCost()).multiply(new BigDecimal(30));


            Integer clientId = plan.getClientId();


            ClientDto clientDto = clientServiceClient.getClientById(clientId);


            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setClientId(clientDto.getId());
            paymentDto.setAmount(cuota);
            paymentDto.setPaymentDate(LocalDateTime.now());
            paymentDto.setPlanId(plan.getId());


            adminServiceClient.notifyPayment(paymentDto);
        }
    }
}
