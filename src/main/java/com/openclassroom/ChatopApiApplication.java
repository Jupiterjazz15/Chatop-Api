package com.openclassroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.openclassroom")
@EnableJpaRepositories(basePackages = "com.openclassroom.repositories")

public class ChatopApiApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(ChatopApiApplication.class, args);
	}

}
