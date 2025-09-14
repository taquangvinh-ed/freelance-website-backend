package com.freelancemarketplace.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.freelancemarketplace.backend.model")
public class FreelancerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FreelancerBackendApplication.class, args);
	}

}
