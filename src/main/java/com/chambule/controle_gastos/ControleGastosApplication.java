package com.chambule.controle_gastos;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ControleGastosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ControleGastosApplication.class, args);
	}

//	@Bean
//	public PasswordEncoder getPassword(){
//		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//		return encoder;
//	}
}
