package com.freelancemarketplace.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImp")
public class FreelancerBackendApplication {

	public static void main(String[] args) {
		System.out.println("Hello World!");
		SpringApplication.run(FreelancerBackendApplication.class, args);
	}

}
