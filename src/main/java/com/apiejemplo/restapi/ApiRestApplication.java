package com.apiejemplo.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.apiejemplo.restapi.configuration.JWTAuthorizationFilter;

@SpringBootApplication
public class ApiRestApplication {

//    @Bean
//    public JWTAuthorizationFilter simpleFilter() {
//        return new JWTAuthorizationFilter();
//    }
//    
	public static void main(String[] args) {
		SpringApplication.run(ApiRestApplication.class, args);
	}

}
