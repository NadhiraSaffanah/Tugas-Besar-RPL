plugins {
    java
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "Sistem manajemen tugas besar untuk dosen dan mahasiswa."

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(22)
	}
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    
    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    
    // Database
    runtimeOnly("org.postgresql:postgresql")
    
    // DevTools
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // TESTING
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}