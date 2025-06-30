package br.com.cotiinformatica.configurations;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
	/*
	 * Ler uma chave de configuração
	 * criada no /application.properties
	 */
	@Value("${queue.name}")
	private String queueName;
	
	@Bean
	Queue queue() {
		/*
		 * true -> fila durável, ou seja, que mantem os dados
		 * salvos mesmo se o servidor for reiniciado
		 */
		return new Queue(queueName, true);
	}
}
