package br.com.cotiinformatica.domain.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.cotiinformatica.domain.dtos.requests.PedidoRequest;
import br.com.cotiinformatica.domain.dtos.responses.PedidoResponse;

public interface PedidoService {
	PedidoResponse criar(PedidoRequest request);
	
	PedidoResponse alterar(UUID id, PedidoRequest request);
	
	PedidoResponse excluir(UUID id);
	
	Page<PedidoResponse> consultar(Pageable pageable);
	
	PedidoResponse obter(UUID id);
}
