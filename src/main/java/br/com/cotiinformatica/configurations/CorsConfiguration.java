package br.com.cotiinformatica.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {
	@Value("${cors.allowed-origins")
	private String[] allowedOrigins;
	
	@Value("${cors.allowed-methods")
	private String[] allowedMethods;
	
	@Value("${cors.allowed-headers")
	private String[] allowedHeaders;
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") //aplicar as configurações para todos os endpoints / rotas
			.allowedOrigins(allowedOrigins) //origens, servidores permitidos
			.allowedMethods(allowedMethods) //métodos permitidos
			.allowedHeaders(allowedHeaders) //cabeçalhos permitidos
			.allowCredentials(true);
	}
}
