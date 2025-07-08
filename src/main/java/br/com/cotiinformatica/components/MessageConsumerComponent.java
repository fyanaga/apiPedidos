package br.com.cotiinformatica.components;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cotiinformatica.domain.entities.Pedido;
import br.com.cotiinformatica.repositories.OutboxMessageRepository;

@Component
public class MessageConsumerComponent {

    private final OutboxMessageRepository outboxMessageRepository;
	
    @Autowired
	private ObjectMapper objectMapper;
	
    @Value("${endpoints.apifaturamentos}")
	private String apiFaturamentos;

    MessageConsumerComponent(OutboxMessageRepository outboxMessageRepository) {
        this.outboxMessageRepository = outboxMessageRepository;
    }
	
	@RabbitListener(queues = "pedidos")
	public void receive(@Payload String message)  throws Exception {
		var pedido = objectMapper.readValue(message, Pedido.class);
		
		var restTemplate = new RestTemplate();
		
		var request = new FaturamentoRequest(pedido.getDataHora(), pedido.getCliente(), pedido.getValor().doubleValue(), pedido.getStatus().toString(), pedido.getId());
		
		var response = restTemplate.exchange(
				apiFaturamentos,
				HttpMethod.POST,
				new HttpEntity<>(request),
				Void.class);
		
		if(!response.getStatusCode().is2xxSuccessful()) {
			throw new IllegalStateException("Não foi possível enviar o faturamento.");
		}
	}
}

record FaturamentoRequest(
		LocalDateTime dataPedido,
		String cliente,
		Double valor,
		String detalhes,
		UUID pedidoId
		){

}