package com.delivery.payment.domain.request;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "orchestrator-payment-request", url = "${url.base.orchestrator-payment}")
public interface OrchestratorPaymentRequest {
                        
        @PostMapping("/orchestrator-payment/notify")
        public void notify(@RequestBody String status);

}
