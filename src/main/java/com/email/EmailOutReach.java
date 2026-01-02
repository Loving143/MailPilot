package com.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class EmailOutReach {

	public static void main(String[] args) {
		SpringApplication.run(EmailOutReach.class, args);
	}

}
