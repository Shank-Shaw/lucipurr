package com.lucipurr.tax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableTransactionManagement
@EnableJpaAuditing
@EnableScheduling
@EntityScan(basePackages = {"com.lucipurr.dbrepo.entity"})
@EnableJpaRepositories(basePackages = "com.lucipurr.dbrepo")
@SpringBootApplication
public class LucipurrApplication {
	public static void main(String[] args) {
		SpringApplication.run(LucipurrApplication.class, args);
	}
}