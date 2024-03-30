package com.yuudong123.chessgalltnmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ChessgalltnmtApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChessgalltnmtApplication.class, args);
	}

}
