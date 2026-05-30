package com.coupon.manager;

import com.coupon.manager.domain.Coupon;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;
import java.time.temporal.TemporalAmount;

@SpringBootApplication
public class ManagerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ManagerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String code;
		String description;
		String expirationDate;
		double discountValue;
		boolean published;

		//Coupon coupon = Coupon.create("abc@1!2-3", "texto texto", Instant.now().plusSeconds(30000).toString(), 0.6, false);

		//System.out.println("Coupon: " + coupon.toString());

	}
}
