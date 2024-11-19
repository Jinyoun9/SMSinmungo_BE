package com.smsinmungo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SmSinmungoApplication {

	public static void main(String[] args) {
		// Spring Boot 애플리케이션 시작
		SpringApplication.run(SmSinmungoApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer configurer() {
		return new WebMvcConfigurer() {
			//CORS 규칙 설정
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**") //모든 경로에 CORS 설정 적용
						.allowedOrigins("*") //모든 도메인을 허용
						.allowedMethods("GET", "POST", "PUT", "DELETE",
								"PATCH"); //GET, POST, PUT, DELETE, PATCH 메서드를 허용
			}
		};
	}

}
