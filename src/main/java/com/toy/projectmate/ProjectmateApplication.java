package com.toy.projectmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ProjectmateApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectmateApplication.class, args);
    }

}
