package com.freelancemarketplace.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(
		basePackages = "com.freelancemarketplace.backend",
		nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class,
		excludeFilters = @ComponentScan.Filter(
				type = FilterType.REGEX,
				pattern = "com\\.freelancemarketplace\\.backend\\.api\\.controller\\..*"
		)
)
@EnableJpaRepositories(
		basePackages = "com.freelancemarketplace.backend",
		nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@EnableJpaAuditing(auditorAwareRef = "auditAwareImp")
@EnableCaching
public class FreelancerBackendApplication {

	public static void main(String[] args) {
		System.out.println("Hello World!");
		SpringApplication.run(FreelancerBackendApplication.class, args);
	}

}
