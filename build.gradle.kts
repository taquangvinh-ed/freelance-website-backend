plugins {
	java
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.freelancemarketplace"
version = "0.0.1-SNAPSHOT"
description = "Back end for freelancer platform"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(23)
	}
}


configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	implementation("org.mapstruct:mapstruct:1.6.3")
	implementation("org.springframework.boot:spring-boot-starter-validation")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
