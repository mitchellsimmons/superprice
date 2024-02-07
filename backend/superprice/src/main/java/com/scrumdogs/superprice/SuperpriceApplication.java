package com.scrumdogs.superprice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SuperpriceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SuperpriceApplication.class, args);
	}

}
