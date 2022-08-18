package com.rafael.peteventos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PetEventosApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetEventosApplication.class, args);
	}

}
