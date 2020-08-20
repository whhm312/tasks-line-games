package me.line.games;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableAspectJAutoProxy
@EnableWebMvc
@SpringBootApplication
public class BoardServierApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardServierApplication.class, args);
	}
}
