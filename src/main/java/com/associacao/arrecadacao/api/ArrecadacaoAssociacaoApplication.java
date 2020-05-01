package com.associacao.arrecadacao.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ArrecadacaoAssociacaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArrecadacaoAssociacaoApplication.class, args);
	}

}
