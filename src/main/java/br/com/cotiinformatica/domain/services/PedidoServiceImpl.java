package br.com.cotiinformatica.domain.services;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.cotiinformatica.components.MessageProducerComponent;
import br.com.cotiinformatica.domain.dtos.requests.PedidoRequest;
import br.com.cotiinformatica.domain.dtos.responses.PedidoResponse;
import br.com.cotiinformatica.domain.entities.Pedido;
import br.com.cotiinformatica.domain.exceptions.PedidoNaoEncontradoException;
import br.com.cotiinformatica.domain.interfaces.PedidoService;
import br.com.cotiinformatica.repositories.PedidoRepository;

@Service
public class PedidoServiceImpl implements PedidoService {
	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private MessageProducerComponent messageProducerComponent;
	
	public PedidoRepository getPedidoRepository() {
		return pedidoRepository;
	}

	public void setPedidoRepository(PedidoRepository pedidoRepository) {
		this.pedidoRepository = pedidoRepository;
	}

	public ModelMapper getModelMapper() {
		return modelMapper;
	}

	public void setModelMapper(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	public MessageProducerComponent getMessageProducerComponent() {
		return messageProducerComponent;
	}

	public void setMessageProducerComponent(MessageProducerComponent messageProducerComponent) {
		this.messageProducerComponent = messageProducerComponent;
	}

	@Override
	public PedidoResponse criar(PedidoRequest request) {
		var pedido = modelMapper.map(request, Pedido.class);
		pedidoRepository.save(pedido);
		messageProducerComponent.send(pedido);

		return modelMapper.map(pedido, PedidoResponse.class);
	}

	@Override
	public PedidoResponse alterar(UUID id, PedidoRequest request) {
		var pedido = pedidoRepository.findById(id)
				.orElseThrow(() -> new
						PedidoNaoEncontradoException(id));

		modelMapper.map(request, pedido);
		pedidoRepository.save(pedido);

		return modelMapper.map(pedido, PedidoResponse.class);
	}

	@Override
	public PedidoResponse excluir(UUID id) {
		var pedido = pedidoRepository.findById(id)
				.orElseThrow(()
						-> new PedidoNaoEncontradoException(id));

		pedidoRepository.delete(pedido);
		return modelMapper.map(pedido, PedidoResponse.class);
	}

	@Override
	public Page<PedidoResponse> consultar(Pageable pageable) {
		var pedidos = pedidoRepository.findAll(pageable);

		return pedidos.map(pedido -> modelMapper
				.map(pedido, PedidoResponse.class));
	}

	@Override
	public PedidoResponse obter(UUID id) {
		var pedido = pedidoRepository.findById(id)

				.orElseThrow(()
						-> new PedidoNaoEncontradoException(id));

		return modelMapper.map(pedido, PedidoResponse.class);
	}

}
