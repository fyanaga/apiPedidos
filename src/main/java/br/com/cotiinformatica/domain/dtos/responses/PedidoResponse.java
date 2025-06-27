package br.com.cotiinformatica.domain.dtos.responses;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PedidoResponse {
	private UUID id;
	
	private String cliente;
	
	private String dataHora;
	
	private Double valor;
	
	private StatusPedidoResponse status;
}
