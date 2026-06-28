package com.ifbank.api_ifbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ApiIfbankApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiIfbankApplication.class, args);
	}

}
