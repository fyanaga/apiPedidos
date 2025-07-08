package br.com.cotiinformatica.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.cotiinformatica.repositories.OutboxMessageRepository;

@Component
public class OutboxProcessorComponent {
	@Autowired
	private OutboxMessageRepository outboxMessageRepository;
	
	@Autowired
	private MessageProducerComponent messageProducerComponent;
	
	@Scheduled(fixedDelay = 5000) //Executado a cada 5 segundos
	public void processarPedidos() {
		//Ler todas as mensagens de pedido criado não enviadas para a fila
		var naoEnviados = outboxMessageRepository.findByNaoEnviadosPorTipo("pedido_criado");
		
		//percorrer os registros e enviar para a fila
		for(var outbox : naoEnviados) {
			try {
				messageProducerComponent.send(outbox.getPayload());
				outbox.setEnviado(true); //modificando o status do pedido na tabela de outbox
				outboxMessageRepository.save(outbox); //atualizando o status no banco
				System.out.println("Pedido enviado para a fila com sucesso: " + outbox.getPayload());
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
