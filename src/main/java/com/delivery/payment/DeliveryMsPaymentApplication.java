package com.delivery.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DeliveryMsPaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryMsPaymentApplication.class, args);
	}

}
