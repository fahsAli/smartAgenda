package com.smartAgenda.authService;

import com.smartAgenda.authService.utils.SslUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthServiceApplication {

	public static void main(String[] args) {

		SslUtil.disableSslVerification();
		SpringApplication.run(AuthServiceApplication.class, args);

	}

}
